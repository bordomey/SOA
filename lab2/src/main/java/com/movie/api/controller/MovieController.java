package com.movie.api.controller;

import com.movie.api.dto.*;
import com.movie.api.model.Movie;
import com.movie.api.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    @GetMapping("/movies")
    public ResponseEntity<MovieListResponse> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer oscarsCount,
            @RequestParam(required = false) Long goldenPalmCount,
            @RequestParam(required = false) Integer length,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) Float coordinateX,
            @RequestParam(required = false) Double coordinateY) {
        
        try {
            // For simplicity, we're not implementing full filtering in this example
            List<Movie> movies = movieService.getAllMovies();
            Long totalElements = (long) movies.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            
            // Apply pagination
            int fromIndex = page * size;
            int toIndex = Math.min(fromIndex + size, movies.size());
            List<Movie> paginatedMovies = movies.subList(fromIndex, toIndex);
            
            MovieListResponse response = new MovieListResponse(paginatedMovies, totalElements, 
                                                              totalPages, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", 
                                                          e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @PostMapping(value = "/movies", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
        try {
            Movie movie = new Movie();
            movie.setName(movieRequest.getName());
            movie.setCoordinates(movieRequest.getCoordinates());
            movie.setOscarsCount(movieRequest.getOscarsCount());
            movie.setGoldenPalmCount(movieRequest.getGoldenPalmCount());
            movie.setLength(movieRequest.getLength());
            movie.setGenre(movieRequest.getGenre());
            movie.setOperator(movieRequest.getOperator());
            
            Movie createdMovie = movieService.createMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", 
                                                          "Invalid input data or constraint violations", 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/movies/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        try {
            Optional<Movie> movie = movieService.getMovieById(id);
            if (movie.isPresent()) {
                return ResponseEntity.ok(movie.get());
            } else {
                ErrorResponse errorResponse = new ErrorResponse("Not Found", 
                                                              "Movie not found", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", 
                                                          "Invalid ID format", 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PutMapping(value = "/movies/{id}", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> updateMovie(@PathVariable Long id, 
                                        @Valid @RequestBody MovieRequest movieRequest) {
        try {
            Movie movieDetails = new Movie();
            movieDetails.setName(movieRequest.getName());
            movieDetails.setCoordinates(movieRequest.getCoordinates());
            movieDetails.setOscarsCount(movieRequest.getOscarsCount());
            movieDetails.setGoldenPalmCount(movieRequest.getGoldenPalmCount());
            movieDetails.setLength(movieRequest.getLength());
            movieDetails.setGenre(movieRequest.getGenre());
            movieDetails.setOperator(movieRequest.getOperator());
            
            Movie updatedMovie = movieService.updateMovie(id, movieDetails);
            return ResponseEntity.ok(updatedMovie);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                ErrorResponse errorResponse = new ErrorResponse("Not Found", 
                                                              "Movie not found", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse("Bad Request", 
                                                              "Invalid input data or constraint violations", 400);
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", 
                                                          "Invalid input data or constraint violations", 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                ErrorResponse errorResponse = new ErrorResponse("Not Found", 
                                                              "Movie not found", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse("Bad Request", 
                                                              "Invalid ID format", 400);
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", 
                                                          "Invalid ID format", 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/movies/average-length")
    public ResponseEntity<AverageResponse> calculateAverageLength() {
        try {
            Double averageLength = movieService.calculateAverageLength();
            AverageResponse response = new AverageResponse(averageLength);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", 
                                                          e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/movies/count-by-operator")
    public ResponseEntity<?> countMoviesByOperator(
            @RequestParam("operator-name") String operatorName,
            @RequestParam(required = false) java.time.LocalDate operatorBirthday,
            @RequestParam(required = false) Long operatorHeight) {
        try {
            Long count = movieService.countMoviesWithOperatorGreaterThan(operatorName, 
                                                                       operatorBirthday, 
                                                                       operatorHeight);
            CountResponse response = new CountResponse(count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", 
                                                          "Invalid operator parameters", 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/movies/filter-by-operator")
    public ResponseEntity<?> filterMoviesByOperator(
            @RequestParam("operator-name") String operatorName,
            @RequestParam(required = false) java.time.LocalDate operatorBirthday,
            @RequestParam(required = false) Long operatorHeight) {
        try {
            List<Movie> movies = movieService.getMoviesWithOperatorGreaterThan(operatorName, 
                                                                             operatorBirthday, 
                                                                             operatorHeight);
            Long totalElements = (long) movies.size();
            
            MovieListResponse response = new MovieListResponse(movies, totalElements, 
                                                              1, 0, movies.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", 
                                                          "Invalid operator parameters", 400);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}