package com.lab2.middleware.controller;

import com.lab2.middleware.model.*;
import com.lab2.movie.soap.MovieService;
import com.lab2.movie.soap.MovieServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/movie-service/api/movies", produces = "application/xml")
public class StatisticsController {

    @Autowired(required = false)
    private MovieService movieService;

    @GetMapping("/average-length")
    public ResponseEntity<?> getAverageLength() {
        if (movieService == null) {
            ErrorResponse error = new ErrorResponse("Service Unavailable", "SOAP service is not available", java.time.ZonedDateTime.now(), 503);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }

        try {
            AverageResponse response = movieService.getAverageLength();
            return ResponseEntity.ok(response);
        } catch (MovieServiceException e) {
            ErrorResponse error = new ErrorResponse("Service Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/count-by-operator")
    public ResponseEntity<?> countMoviesByOperator(
            @RequestParam("operator-name") String operatorName,
            @RequestParam(value = "operator-birthday", required = false) String operatorBirthday,
            @RequestParam(value = "operator-height", required = false) Long operatorHeight) {
        
        if (movieService == null) {
            ErrorResponse error = new ErrorResponse("Service Unavailable", "SOAP service is not available", java.time.ZonedDateTime.now(), 503);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }

        try {
            CountResponse response = movieService.countMoviesByOperator(operatorName, operatorBirthday, operatorHeight);
            return ResponseEntity.ok(response);
        } catch (MovieServiceException e) {
            ErrorResponse error = new ErrorResponse("Service Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/filter-by-operator")
    public ResponseEntity<?> filterMoviesByOperator(
            @RequestParam("operator-name") String operatorName,
            @RequestParam(value = "operator-birthday", required = false) String operatorBirthday,
            @RequestParam(value = "operator-height", required = false) Long operatorHeight) {
        
        if (movieService == null) {
            ErrorResponse error = new ErrorResponse("Service Unavailable", "SOAP service is not available", java.time.ZonedDateTime.now(), 503);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }

        try {
            MovieListResponse response = movieService.filterMoviesByOperator(operatorName, operatorBirthday, operatorHeight);
            return ResponseEntity.ok(response);
        } catch (MovieServiceException e) {
            ErrorResponse error = new ErrorResponse("Service Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}