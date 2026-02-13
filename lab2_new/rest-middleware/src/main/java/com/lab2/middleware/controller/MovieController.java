package com.lab2.middleware.controller;

import com.lab2.middleware.model.*;
import com.lab2.movie.soap.MovieService;
import com.lab2.movie.soap.MovieServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/movie-service/api/movies", produces = "application/xml")
public class MovieController {

    @Autowired(required = false)
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<?> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) List<String> sort,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String oscarsCount,
            @RequestParam(required = false) String goldenPalmCount,
            @RequestParam(required = false) String length,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) String x,
            @RequestParam(required = false) String y) {

        if (movieService == null) {
            ErrorResponse error = new ErrorResponse("Service Unavailable", "SOAP service is not available", java.time.ZonedDateTime.now(), 503);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }

        try {
            MovieListResponse response = movieService.getAllMovies(
                    page, size, sort, id, name, oscarsCount, goldenPalmCount, 
                    length, genre, operatorName, x, y);
            return ResponseEntity.ok(response);
        } catch (MovieServiceException e) {
            ErrorResponse error = new ErrorResponse("Service Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody MovieRequest movieRequest) {
        if (movieService == null) {
            ErrorResponse error = new ErrorResponse("Service Unavailable", "SOAP service is not available", java.time.ZonedDateTime.now(), 503);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }

        try {
            Object result = movieService.createMovie(movieRequest);
            if (result instanceof ErrorResponse) {
                ErrorResponse error = (ErrorResponse) result;
                return ResponseEntity.status(HttpStatus.valueOf(error.getStatus())).body(error);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (MovieServiceException e) {
            ErrorResponse error = new ErrorResponse("Service Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable long id) {
        if (movieService == null) {
            ErrorResponse error = new ErrorResponse("Service Unavailable", "SOAP service is not available", java.time.ZonedDateTime.now(), 503);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }

        try {
            Object result = movieService.getMovieById(id);
            if (result instanceof ErrorResponse) {
                ErrorResponse error = (ErrorResponse) result;
                return ResponseEntity.status(HttpStatus.valueOf(error.getStatus())).body(error);
            }
            return ResponseEntity.ok(result);
        } catch (MovieServiceException e) {
            ErrorResponse error = new ErrorResponse("Service Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable long id, @RequestBody MovieRequest movieRequest) {
        if (movieService == null) {
            ErrorResponse error = new ErrorResponse("Service Unavailable", "SOAP service is not available", java.time.ZonedDateTime.now(), 503);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }

        try {
            Object result = movieService.updateMovie(id, movieRequest);
            if (result instanceof ErrorResponse) {
                ErrorResponse error = (ErrorResponse) result;
                return ResponseEntity.status(HttpStatus.valueOf(error.getStatus())).body(error);
            }
            return ResponseEntity.ok(result);
        } catch (MovieServiceException e) {
            ErrorResponse error = new ErrorResponse("Service Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable long id) {
        if (movieService == null) {
            ErrorResponse error = new ErrorResponse("Service Unavailable", "SOAP service is not available", java.time.ZonedDateTime.now(), 503);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }

        try {
            boolean result = movieService.deleteMovie(id);
            if (result) {
                return ResponseEntity.noContent().build();
            } else {
                ErrorResponse error = new ErrorResponse("Not Found", "Movie not found with ID: " + id, java.time.ZonedDateTime.now(), 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (MovieServiceException e) {
            ErrorResponse error = new ErrorResponse("Service Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PatchMapping("/honor-by-length/{minLength}/oscars-to-add")
    public ResponseEntity<?> addOscarsToMoviesByLength(
            @PathVariable int minLength,
            @RequestParam int oscarsToAdd) {
        if (movieService == null) {
            ErrorResponse error = new ErrorResponse("Service Unavailable", "SOAP service is not available", java.time.ZonedDateTime.now(), 503);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }

        try {
            OscarUpdateResponse response = movieService.addOscarsToMoviesByLength(minLength, oscarsToAdd);
            return ResponseEntity.ok(response);
        } catch (MovieServiceException e) {
            ErrorResponse error = new ErrorResponse("Service Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}