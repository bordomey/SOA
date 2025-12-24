package com.lab2.movie.controller;

import com.lab2.movie.dao.MovieDAO;
import com.lab2.movie.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("movie-service/api/movies")
public class StatisticsController {
    private MovieDAO movieDAO = MovieDAO.getInstance();

    @GetMapping("/average-length")
    public ResponseEntity<AverageResponse> getAverageLength() {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            if (movies.isEmpty()) {
                AverageResponse response = new AverageResponse(0.0);
                return ResponseEntity.ok(response);
            }

            double average = movies.stream()
                    .mapToInt(Movie::getLength)
                    .average()
                    .orElse(0.0);

            AverageResponse response = new AverageResponse(average);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/count-by-operator")
    public ResponseEntity<CountResponse> countMoviesByOperator(
            @RequestParam("operator-name") String operatorName,
            @RequestParam(value = "operator-birthday", required = false) String operatorBirthdayStr,
            @RequestParam(value = "operator-height", required = false) Long operatorHeight) {
        try {
            if (operatorName == null || operatorName.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Person operator = new Person();
            operator.setName(operatorName);
            
            if (operatorBirthdayStr != null && !operatorBirthdayStr.trim().isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate birthday = LocalDate.parse(operatorBirthdayStr, formatter);
                    operator.setBirthday(birthday.atStartOfDay().atZone(java.time.ZoneId.systemDefault()));
                } catch (Exception e) {
                    return ResponseEntity.badRequest().build();
                }
            }
            
            operator.setHeight(operatorHeight);

            long count = countMoviesWithGreaterOperator(operator);
            CountResponse response = new CountResponse(count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/filter-by-operator")
    public ResponseEntity<MovieListResponse> filterMoviesByOperator(
            @RequestParam("operator-name") String operatorName,
            @RequestParam(value = "operator-birthday", required = false) String operatorBirthdayStr,
            @RequestParam(value = "operator-height", required = false) Long operatorHeight) {
        try {
            if (operatorName == null || operatorName.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Person operator = new Person();
            operator.setName(operatorName);
            
            if (operatorBirthdayStr != null && !operatorBirthdayStr.trim().isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate birthday = LocalDate.parse(operatorBirthdayStr, formatter);
                    operator.setBirthday(birthday.atStartOfDay().atZone(java.time.ZoneId.systemDefault()));
                } catch (Exception e) {
                    return ResponseEntity.badRequest().build();
                }
            }
            
            operator.setHeight(operatorHeight);

            List<Movie> movies = getMoviesWithGreaterOperator(operator);
            MovieListResponse response = new MovieListResponse(movies, movies.size(), 1, 0, movies.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private long countMoviesWithGreaterOperator(Person operator) {
        return movieDAO.getAllMovies().stream()
                .filter(movie -> isOperatorGreaterThan(movie.getOperator(), operator))
                .count();
    }

    private List<Movie> getMoviesWithGreaterOperator(Person operator) {
        return movieDAO.getAllMovies().stream()
                .filter(movie -> isOperatorGreaterThan(movie.getOperator(), operator))
                .collect(Collectors.toList());
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