package com.lab2.middleware.controller;

import com.lab2.middleware.model.*;
import com.lab2.movie.soap.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie-service/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

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
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) String x,
            @RequestParam(required = false) String y) {

        MovieListResponse response = movieService.getAllMovies(
                page, size, sort, id, name, oscarsCount, goldenPalmCount, 
                length, genre, operatorName, x, y);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody MovieRequest movieRequest) {
        Object result = movieService.createMovie(movieRequest);
        if (result instanceof ErrorResponse) {
            ErrorResponse error = (ErrorResponse) result;
            return ResponseEntity.status(HttpStatus.valueOf(error.getStatus())).body(error);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable long id) {
        Object result = movieService.getMovieById(id);
        if (result instanceof ErrorResponse) {
            ErrorResponse error = (ErrorResponse) result;
            return ResponseEntity.status(HttpStatus.valueOf(error.getStatus())).body(error);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable long id, @RequestBody MovieRequest movieRequest) {
        Object result = movieService.updateMovie(id, movieRequest);
        if (result instanceof ErrorResponse) {
            ErrorResponse error = (ErrorResponse) result;
            return ResponseEntity.status(HttpStatus.valueOf(error.getStatus())).body(error);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable long id) {
        boolean result = movieService.deleteMovie(id);
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            ErrorResponse error = new ErrorResponse("Not Found", "Movie not found with ID: " + id, java.time.ZonedDateTime.now(), 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PatchMapping("/honor-by-length/{minLength}/oscars-to-add")
    public ResponseEntity<OscarUpdateResponse> addOscarsToMoviesByLength(
            @PathVariable int minLength,
            @RequestParam int oscarsToAdd) {
        OscarUpdateResponse response = movieService.addOscarsToMoviesByLength(minLength, oscarsToAdd);
        return ResponseEntity.ok(response);
    }
}