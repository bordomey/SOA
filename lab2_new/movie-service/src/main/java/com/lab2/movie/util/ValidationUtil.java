package com.lab2.movie.util;

import com.lab2.movie.model.Movie;
import com.lab2.movie.model.Coordinates;
import com.lab2.movie.model.Person;

public class ValidationUtil {
    
    public static String validateMovie(Movie movie) {
        if (movie == null) {
            return "Movie object cannot be null";
        }
        
        if (movie.getName() == null || movie.getName().trim().isEmpty()) {
            return "Movie name cannot be null or empty";
        }
        
        if (movie.getCoordinates() == null) {
            return "Movie coordinates cannot be null";
        }
        
        String coordinateError = validateCoordinates(movie.getCoordinates());
        if (coordinateError != null) {
            return coordinateError;
        }
        
        if (movie.getOscarsCount() == null || movie.getOscarsCount() < 0) {
            return "Movie oscarsCount must be greater than 0";
        }
        
        if (movie.getGoldenPalmCount() == null || movie.getGoldenPalmCount() < 0) {
            return "Movie goldenPalmCount must be greater than 0";
        }
        
        if (movie.getLength() <= 0) {
            return "Movie length must be greater than 0";
        }
        
        if (movie.getOperator() == null) {
            return "Movie operator cannot be null";
        }
        
        String personError = validatePerson(movie.getOperator());
        if (personError != null) {
            return personError;
        }
        
        return null; // No validation errors
    }
    
    private static String validateCoordinates(Coordinates coordinates) {
        if (coordinates.getX() == null) {
            return "Coordinate x cannot be null";
        }
        
        if (coordinates.getY() <= -26) {
            return "Coordinate y must be greater than -26";
        }
        
        return null; // No validation errors
    }
    
    private static String validatePerson(Person person) {
        if (person.getName() == null || person.getName().trim().isEmpty()) {
            return "Person name cannot be null or empty";
        }
        
        if (person.getHeight() != null && person.getHeight() <= 0) {
            return "Person height must be greater than 0";
        }
        
        return null; // No validation errors
    }
}