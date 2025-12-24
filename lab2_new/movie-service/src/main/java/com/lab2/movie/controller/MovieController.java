package com.lab2.movie.controller;

import com.lab2.movie.dao.MovieDAO;
import com.lab2.movie.model.*;
import com.lab2.movie.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movie-service/api/movies")
public class MovieController {
    private MovieDAO movieDAO = MovieDAO.getInstance();
    
    // Pattern to match range expressions like (1,5], [1,5), (1,5), [1,5]
    private static final Pattern RANGE_PATTERN = Pattern.compile("([\\(\\[])(-?\\d+(?:\\.\\d+)?),(-?\\d+(?:\\.\\d+)?)([\\)\\]])");

    @GetMapping
    public ResponseEntity<MovieListResponse> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) List<String> sort,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String oscarsCount,
            @RequestParam(required = false) String goldenPalmCount,
            @RequestParam(required = false) String length,
            @RequestParam(required = false) MovieGenre genre,
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) String x,
            @RequestParam(required = false) String y) {

        try {
            List<Movie> allMovies = movieDAO.getAllMovies();

            // Apply filters
            List<Movie> filteredMovies = allMovies.stream()
                    .filter(createLongFilter(movie -> movie.getId(), id))
                    .filter(movie -> name == null || movie.getName().contains(name))
                    .filter(createIntegerFilter(movie -> movie.getOscarsCount(), oscarsCount))
                    .filter(createLongFilter(movie -> movie.getGoldenPalmCount(), goldenPalmCount))
                    .filter(createIntegerFilter(movie -> movie.getLength(), length))
                    .filter(movie -> genre == null || movie.getGenre() == genre)
                    .filter(movie -> operatorName == null || movie.getOperator().getName().contains(operatorName))
                    .filter(createFloatFilter(movie -> movie.getCoordinates().getX(), x))
                    .filter(createDoubleFilter(movie -> movie.getCoordinates().getY(), y))
                    .collect(Collectors.toList());

            // Apply sorting
            if (sort != null && !sort.isEmpty()) {
                for (String sortParam : sort) {
                    String[] parts = sortParam.split(",");
                    String field = parts[0];
                    String direction = parts.length > 1 ? parts[1].toLowerCase() : "asc";

                    Comparator<Movie> comparator = null;
                    switch (field) {
                        case "id":
                            comparator = Comparator.comparingLong(Movie::getId);
                            break;
                        case "name":
                            comparator = Comparator.comparing(Movie::getName);
                            break;
                        case "length":
                            comparator = Comparator.comparingInt(Movie::getLength);
                            break;
                        case "oscarsCount":
                            comparator = Comparator.comparing(Movie::getOscarsCount);
                            break;
                        case "goldenPalmCount":
                            comparator = Comparator.comparing(Movie::getGoldenPalmCount);
                            break;
                    }

                    if (comparator != null) {
                        if ("desc".equals(direction)) {
                            comparator = comparator.reversed();
                        }
                        filteredMovies.sort(comparator);
                    }
                }
            }

            // Apply pagination
            int totalElements = filteredMovies.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            int fromIndex = page * size;
            int toIndex = Math.min(fromIndex + size, totalElements);

            List<Movie> pagedMovies = fromIndex < totalElements ?
                    filteredMovies.subList(fromIndex, toIndex) : List.of();

            MovieListResponse response = new MovieListResponse(pagedMovies, totalElements, totalPages, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MovieListResponse());
        }
    }

    // Helper method to create filter for Long values (either exact value or range)
    private Predicate<Movie> createLongFilter(java.util.function.Function<Movie, Long> getter, String valueOrRange) {
        if (valueOrRange == null || valueOrRange.isEmpty()) {
            return movie -> true; // No filter
        }
        
        // Check if it's a range expression
        Matcher matcher = RANGE_PATTERN.matcher(valueOrRange);
        if (matcher.matches()) {
            return parseLongRangeExpression(getter, matcher);
        } else {
            // It's an exact value
            try {
                long exactValue = Long.parseLong(valueOrRange);
                return movie -> {
                    Long value = getter.apply(movie);
                    return value != null && value.equals(exactValue);
                };
            } catch (NumberFormatException e) {
                return movie -> false; // Invalid value
            }
        }
    }

    // Helper method to create filter for Integer values (either exact value or range)
    private Predicate<Movie> createIntegerFilter(java.util.function.Function<Movie, Integer> getter, String valueOrRange) {
        if (valueOrRange == null || valueOrRange.isEmpty()) {
            return movie -> true; // No filter
        }
        
        // Check if it's a range expression
        Matcher matcher = RANGE_PATTERN.matcher(valueOrRange);
        if (matcher.matches()) {
            return parseIntegerRangeExpression(getter, matcher);
        } else {
            // It's an exact value
            try {
                int exactValue = Integer.parseInt(valueOrRange);
                return movie -> {
                    Integer value = getter.apply(movie);
                    return value != null && value.equals(exactValue);
                };
            } catch (NumberFormatException e) {
                return movie -> false; // Invalid value
            }
        }
    }

    // Helper method to create filter for Float values (either exact value or range)
    private Predicate<Movie> createFloatFilter(java.util.function.Function<Movie, Float> getter, String valueOrRange) {
        if (valueOrRange == null || valueOrRange.isEmpty()) {
            return movie -> true; // No filter
        }
        
        // Check if it's a range expression
        Matcher matcher = RANGE_PATTERN.matcher(valueOrRange);
        if (matcher.matches()) {
            return parseFloatRangeExpression(getter, matcher);
        } else {
            // It's an exact value
            try {
                float exactValue = Float.parseFloat(valueOrRange);
                return movie -> {
                    Float value = getter.apply(movie);
                    return value != null && Math.abs(value - exactValue) < 0.001;
                };
            } catch (NumberFormatException e) {
                return movie -> false; // Invalid value
            }
        }
    }

    // Helper method to create filter for Double values (either exact value or range)
    private Predicate<Movie> createDoubleFilter(java.util.function.Function<Movie, Double> getter, String valueOrRange) {
        if (valueOrRange == null || valueOrRange.isEmpty()) {
            return movie -> true; // No filter
        }
        
        // Check if it's a range expression
        Matcher matcher = RANGE_PATTERN.matcher(valueOrRange);
        if (matcher.matches()) {
            return parseDoubleRangeExpression(getter, matcher);
        } else {
            // It's an exact value
            try {
                double exactValue = Double.parseDouble(valueOrRange);
                return movie -> {
                    Double value = getter.apply(movie);
                    return value != null && Math.abs(value - exactValue) < 0.001;
                };
            } catch (NumberFormatException e) {
                return movie -> false; // Invalid value
            }
        }
    }

    // Parse long range expression like (1,5], [1,5), (1,5), [1,5]
    private Predicate<Movie> parseLongRangeExpression(java.util.function.Function<Movie, Long> getter, Matcher matcher) {
        boolean includeLower = "[".equals(matcher.group(1));
        boolean includeUpper = "]".equals(matcher.group(4));
        long lowerBound = Long.parseLong(matcher.group(2));
        long upperBound = Long.parseLong(matcher.group(3));
        
        return movie -> {
            Long value = getter.apply(movie);
            if (value == null) return false;
            
            boolean lowerCondition = includeLower ? value >= lowerBound : value > lowerBound;
            boolean upperCondition = includeUpper ? value <= upperBound : value < upperBound;
            
            return lowerCondition && upperCondition;
        };
    }

    // Parse integer range expression like (1,5], [1,5), (1,5), [1,5]
    private Predicate<Movie> parseIntegerRangeExpression(java.util.function.Function<Movie, Integer> getter, Matcher matcher) {
        boolean includeLower = "[".equals(matcher.group(1));
        boolean includeUpper = "]".equals(matcher.group(4));
        int lowerBound = Integer.parseInt(matcher.group(2));
        int upperBound = Integer.parseInt(matcher.group(3));
        
        return movie -> {
            Integer value = getter.apply(movie);
            if (value == null) return false;
            
            boolean lowerCondition = includeLower ? value >= lowerBound : value > lowerBound;
            boolean upperCondition = includeUpper ? value <= upperBound : value < upperBound;
            
            return lowerCondition && upperCondition;
        };
    }

    // Parse float range expression like (1.5,5.7], [1.2,5.8), (1.1,5.9), [1.0,5.5]
    private Predicate<Movie> parseFloatRangeExpression(java.util.function.Function<Movie, Float> getter, Matcher matcher) {
        boolean includeLower = "[".equals(matcher.group(1));
        boolean includeUpper = "]".equals(matcher.group(4));
        float lowerBound = Float.parseFloat(matcher.group(2));
        float upperBound = Float.parseFloat(matcher.group(3));
        
        return movie -> {
            Float value = getter.apply(movie);
            if (value == null) return false;
            
            boolean lowerCondition = includeLower ? value >= lowerBound : value > lowerBound;
            boolean upperCondition = includeUpper ? value <= upperBound : value < upperBound;
            
            return lowerCondition && upperCondition;
        };
    }

    // Parse double range expression like (1.5,5.7], [1.2,5.8), (1.1,5.9), [1.0,5.5]
    private Predicate<Movie> parseDoubleRangeExpression(java.util.function.Function<Movie, Double> getter, Matcher matcher) {
        boolean includeLower = "[".equals(matcher.group(1));
        boolean includeUpper = "]".equals(matcher.group(4));
        double lowerBound = Double.parseDouble(matcher.group(2));
        double upperBound = Double.parseDouble(matcher.group(3));
        
        return movie -> {
            Double value = getter.apply(movie);
            if (value == null) return false;
            
            boolean lowerCondition = includeLower ? value >= lowerBound : value > lowerBound;
            boolean upperCondition = includeUpper ? value <= upperBound : value < upperBound;
            
            return lowerCondition && upperCondition;
        };
    }

    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody MovieRequest movieRequest) {
        try {
            // Convert MovieRequest to Movie
            Movie movie = movieRequest.toMovie();
            
            // Validate movie
            String validationError = ValidationUtil.validateMovie(movie);
            if (validationError != null) {
                ErrorResponse error = new ErrorResponse("Bad Request: " + validationError, validationError, java.time.ZonedDateTime.now(), 400);
                return ResponseEntity.badRequest().body(error);
            }

            Movie createdMovie = movieDAO.createMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable long id) {
        try {
            if (id <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "ID must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return ResponseEntity.badRequest().body(error);
            }

            Movie movie = movieDAO.getMovieById(id);
            if (movie == null) {
                ErrorResponse error = new ErrorResponse("Not Found", "Movie not found with ID: " + id, java.time.ZonedDateTime.now(), 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            return ResponseEntity.ok(movie);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable long id, @RequestBody MovieRequest movieRequest) {
        try {
            if (id <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "ID must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return ResponseEntity.badRequest().body(error);
            }

            // Convert MovieRequest to Movie
            Movie movie = movieRequest.toMovie();
            
            // Validate movie
            String validationError = ValidationUtil.validateMovie(movie);
            if (validationError != null) {
                ErrorResponse error = new ErrorResponse("Bad Request", validationError, java.time.ZonedDateTime.now(), 400);
                return ResponseEntity.badRequest().body(error);
            }

            Movie updatedMovie = movieDAO.updateMovie(id, movie);
            if (updatedMovie == null) {
                ErrorResponse error = new ErrorResponse("Not Found", "Movie not found with ID: " + id, java.time.ZonedDateTime.now(), 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            return ResponseEntity.ok(updatedMovie);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable long id) {
        try {
            if (id <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "ID must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return ResponseEntity.badRequest().body(error);
            }

            boolean deleted = movieDAO.deleteMovie(id);
            if (!deleted) {
                ErrorResponse error = new ErrorResponse("Not Found", "Movie not found with ID: " + id, java.time.ZonedDateTime.now(), 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PatchMapping("/honor-by-length/{minLength}/oscars-to-add")
    public ResponseEntity<OscarUpdateResponse> addOscarsToMoviesByLength(
            @PathVariable int minLength,
            @RequestParam int oscarsToAdd) {
        try {
            if (minLength <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Minimum length must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return ResponseEntity.badRequest().body(new OscarUpdateResponse());
            }
            
            if (oscarsToAdd <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Oscars to add must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return ResponseEntity.badRequest().body(new OscarUpdateResponse());
            }

            int moviesAffected = 0;
            List<Movie> movies = movieDAO.getAllMovies();
            for (Movie movie : movies) {
                if (movie.getLength() > minLength) {
                    movie.setOscarsCount(movie.getOscarsCount() + oscarsToAdd);
                    moviesAffected++;
                }
            }
            
            // Update all affected movies
            for (Movie movie : movies) {
                if (movie.getLength() > minLength) {
                    movieDAO.updateMovie(movie.getId(), movie);
                }
            }

            OscarUpdateResponse response = new OscarUpdateResponse();
            response.setUpdatedMoviesCount(moviesAffected);
            response.setMessage("Successfully added " + oscarsToAdd + " Oscars to " + moviesAffected + " qualifying movies");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OscarUpdateResponse());
        }
    }
}