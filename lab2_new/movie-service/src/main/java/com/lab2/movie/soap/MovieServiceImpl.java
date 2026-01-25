package com.lab2.movie.soap;

import com.lab2.movie.dao.MovieDAO;
import com.lab2.movie.model.*;
import com.lab2.movie.util.ValidationUtil;

import javax.jws.WebService;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@WebService(endpointInterface = "com.lab2.movie.soap.MovieService", 
            serviceName = "MovieService", 
            targetNamespace = "http://soap.movie.lab2.com/")
public class MovieServiceImpl implements MovieService {
    private MovieDAO movieDAO = MovieDAO.getInstance();
    
    // Pattern to match range expressions like (1,5], [1,5), (1,5), [1,5]
    private static final Pattern RANGE_PATTERN = Pattern.compile("([\\(\\[])(-?\\d+(?:\\.\\d+)?),(-?\\d+(?:\\.\\d+)?)([\\)\\]])");

    @Override
    public MovieListResponse getAllMovies(
            int page, int size, List<String> sort,
            String id, String name, String oscarsCount, String goldenPalmCount,
            String length, String genre, String operatorName, String x, String y) {

        try {
            List<Movie> allMovies = movieDAO.getAllMovies();

            // Apply filters
            List<Movie> filteredMovies = allMovies.stream()
                    .filter(createLongFilter(movie -> movie.getId(), id))
                    .filter(movie -> name == null || movie.getName().contains(name))
                    .filter(createIntegerFilter(movie -> movie.getOscarsCount(), oscarsCount))
                    .filter(createLongFilter(movie -> movie.getGoldenPalmCount(), goldenPalmCount))
                    .filter(createIntegerFilter(movie -> movie.getLength(), length))
                    .filter(movie -> genre == null || movie.getGenre().toString().equals(genre))
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

            return new MovieListResponse(pagedMovies, totalElements, totalPages, page, size);
        } catch (Exception e) {
            return new MovieListResponse();
        }
    }

    @Override
    public Object createMovie(MovieRequest movieRequest) {
        try {
            // Convert MovieRequest to Movie
            Movie movie = movieRequest.toMovie();
            
            // Validate movie
            String validationError = ValidationUtil.validateMovie(movie);
            if (validationError != null) {
                ErrorResponse error = new ErrorResponse("Bad Request: " + validationError, validationError, java.time.ZonedDateTime.now(), 400);
                return error;
            }

            Movie createdMovie = movieDAO.createMovie(movie);
            return createdMovie;
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return error;
        }
    }

    @Override
    public Object getMovieById(long id) {
        try {
            if (id <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "ID must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return error;
            }

            Movie movie = movieDAO.getMovieById(id);
            if (movie == null) {
                ErrorResponse error = new ErrorResponse("Not Found", "Movie not found with ID: " + id, java.time.ZonedDateTime.now(), 404);
                return error;
            }

            return movie;
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return error;
        }
    }

    @Override
    public Object updateMovie(long id, MovieRequest movieRequest) {
        try {
            if (id <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "ID must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return error;
            }

            // Convert MovieRequest to Movie
            Movie movie = movieRequest.toMovie();
            
            // Validate movie
            String validationError = ValidationUtil.validateMovie(movie);
            if (validationError != null) {
                ErrorResponse error = new ErrorResponse("Bad Request", validationError, java.time.ZonedDateTime.now(), 400);
                return error;
            }

            Movie updatedMovie = movieDAO.updateMovie(id, movie);
            if (updatedMovie == null) {
                ErrorResponse error = new ErrorResponse("Not Found", "Movie not found with ID: " + id, java.time.ZonedDateTime.now(), 404);
                return error;
            }

            return updatedMovie;
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return error;
        }
    }

    @Override
    public boolean deleteMovie(long id) {
        try {
            if (id <= 0) {
                return false;
            }

            return movieDAO.deleteMovie(id);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public OscarUpdateResponse addOscarsToMoviesByLength(int minLength, int oscarsToAdd) {
        try {
            if (minLength <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Minimum length must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return new OscarUpdateResponse();
            }
            
            if (oscarsToAdd <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Oscars to add must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return new OscarUpdateResponse();
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
            return response;
        } catch (Exception e) {
            return new OscarUpdateResponse();
        }
    }

    @Override
    public AverageResponse getAverageLength() {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            if (movies.isEmpty()) {
                return new AverageResponse(0.0);
            }

            double average = movies.stream()
                    .mapToInt(Movie::getLength)
                    .average()
                    .orElse(0.0);

            return new AverageResponse(average);
        } catch (Exception e) {
            return new AverageResponse(0.0);
        }
    }

    @Override
    public CountResponse countMoviesByOperator(String operatorName, String operatorBirthday, Long operatorHeight) {
        try {
            if (operatorName == null || operatorName.trim().isEmpty()) {
                return new CountResponse(0);
            }

            Person operator = new Person();
            operator.setName(operatorName);
            
            if (operatorBirthday != null && !operatorBirthday.trim().isEmpty()) {
                try {
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    java.time.LocalDate birthday = java.time.LocalDate.parse(operatorBirthday, formatter);
                    operator.setBirthday(birthday.atStartOfDay().atZone(java.time.ZoneId.systemDefault()));
                } catch (Exception e) {
                    return new CountResponse(0);
                }
            }
            
            operator.setHeight(operatorHeight);

            long count = countMoviesWithGreaterOperator(operator);
            return new CountResponse(count);
        } catch (Exception e) {
            return new CountResponse(0);
        }
    }

    @Override
    public MovieListResponse filterMoviesByOperator(String operatorName, String operatorBirthday, Long operatorHeight) {
        try {
            if (operatorName == null || operatorName.trim().isEmpty()) {
                return new MovieListResponse();
            }

            Person operator = new Person();
            operator.setName(operatorName);
            
            if (operatorBirthday != null && !operatorBirthday.trim().isEmpty()) {
                try {
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    java.time.LocalDate birthday = java.time.LocalDate.parse(operatorBirthday, formatter);
                    operator.setBirthday(birthday.atStartOfDay().atZone(java.time.ZoneId.systemDefault()));
                } catch (Exception e) {
                    return new MovieListResponse();
                }
            }
            
            operator.setHeight(operatorHeight);

            List<Movie> movies = getMoviesWithGreaterOperator(operator);
            return new MovieListResponse(movies, movies.size(), 1, 0, movies.size());
        } catch (Exception e) {
            return new MovieListResponse();
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

    private long countMoviesWithGreaterOperator(Person operator) {
        return movieDAO.getAllMovies().stream()
                .filter(movie -> isOperatorGreaterThan(movie.getOperator(), operator))
                .count();
    }

    private List<Movie> getMoviesWithGreaterOperator(Person operator) {
        return movieDAO.getAllMovies().stream()
                .filter(movie -> isOperatorGreaterThan(movie.getOperator(), operator))
                .collect(java.util.stream.Collectors.toList());
    }

    private boolean isOperatorGreaterThan(Person movieOperator, Person compareOperator) {
        // Compare by name first
        int nameComparison = movieOperator.getName().compareTo(compareOperator.getName());
        if (nameComparison != 0) {
            return nameComparison < 0;
        }

        // If names are equal, compare by birthday
        if (compareOperator.getBirthday() != null) {
            if (movieOperator.getBirthday() == null) {
                return false; // null is considered less than any value
            }
            int birthdayComparison = movieOperator.getBirthday().compareTo(compareOperator.getBirthday());
            if (birthdayComparison != 0) {
                return birthdayComparison > 0;
            }
        }

        // If birthdays are equal or compareOperator's birthday is null, compare by height
        if (compareOperator.getHeight() != null) {
            if (movieOperator.getHeight() == null) {
                return false; // null is considered less than any value
            }
            return movieOperator.getHeight() > compareOperator.getHeight();
        }

        // If all compared fields are equal or null, movieOperator is not greater
        return false;
    }
}