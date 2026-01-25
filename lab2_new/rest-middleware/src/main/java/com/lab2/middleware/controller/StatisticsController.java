package com.lab2.middleware.controller;

import com.lab2.middleware.model.*;
import com.lab2.movie.soap.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie-service/api/movies")
public class StatisticsController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/average-length")
    public ResponseEntity<AverageResponse> getAverageLength() {
        AverageResponse response = movieService.getAverageLength();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count-by-operator")
    public ResponseEntity<CountResponse> countMoviesByOperator(
            @RequestParam("operator-name") String operatorName,
            @RequestParam(value = "operator-birthday", required = false) String operatorBirthday,
            @RequestParam(value = "operator-height", required = false) Long operatorHeight) {
        
        CountResponse response = movieService.countMoviesByOperator(operatorName, operatorBirthday, operatorHeight);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter-by-operator")
    public ResponseEntity<MovieListResponse> filterMoviesByOperator(
            @RequestParam("operator-name") String operatorName,
            @RequestParam(value = "operator-birthday", required = false) String operatorBirthday,
            @RequestParam(value = "operator-height", required = false) Long operatorHeight) {
        
        MovieListResponse response = movieService.filterMoviesByOperator(operatorName, operatorBirthday, operatorHeight);
        return ResponseEntity.ok(response);
    }
}