package com.lab2.movie.resource;

import com.lab2.movie.dao.MovieDAO;
import com.lab2.movie.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("/movies")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public class MovieStatisticsResource {
    private MovieDAO movieDAO = MovieDAO.getInstance();

    @GET
    @Path("/average-length")
    public Response getAverageLength() {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            if (movies.isEmpty()) {
                AverageResponse response = new AverageResponse(0.0);
                return Response.ok(response).build();
            }

            double average = movies.stream()
                    .mapToInt(Movie::getLength)
                    .average()
                    .orElse(0.0);

            AverageResponse response = new AverageResponse(average);
            return Response.ok(response).build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    @GET
    @Path("/count-by-operator")
    public Response countMoviesByOperator(
            @QueryParam("operator-name") String operatorName,
            @QueryParam("operator-birthday") String operatorBirthdayStr,
            @QueryParam("operator-height") Long operatorHeight) {
        try {
            if (operatorName == null || operatorName.trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Operator name is required", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            Person operator = new Person();
            operator.setName(operatorName);
            
            if (operatorBirthdayStr != null && !operatorBirthdayStr.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthday = sdf.parse(operatorBirthdayStr);
                    operator.setBirthday(birthday);
                } catch (ParseException e) {
                    ErrorResponse error = new ErrorResponse("Bad Request", "Invalid birthday format. Expected yyyy-MM-dd", java.time.ZonedDateTime.now(), 400);
                    return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
                }
            }
            
            operator.setHeight(operatorHeight);

            long count = countMoviesWithGreaterOperator(operator);
            CountResponse response = new CountResponse(count);
            return Response.ok(response).build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    @GET
    @Path("/filter-by-operator")
    public Response filterMoviesByOperator(
            @QueryParam("operator-name") String operatorName,
            @QueryParam("operator-birthday") String operatorBirthdayStr,
            @QueryParam("operator-height") Long operatorHeight) {
        try {
            if (operatorName == null || operatorName.trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Operator name is required", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            Person operator = new Person();
            operator.setName(operatorName);
            
            if (operatorBirthdayStr != null && !operatorBirthdayStr.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthday = sdf.parse(operatorBirthdayStr);
                    operator.setBirthday(birthday);
                } catch (ParseException e) {
                    ErrorResponse error = new ErrorResponse("Bad Request", "Invalid birthday format. Expected yyyy-MM-dd", java.time.ZonedDateTime.now(), 400);
                    return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
                }
            }
            
            operator.setHeight(operatorHeight);

            List<Movie> movies = getMoviesWithGreaterOperator(operator);
            MovieListResponse response = new MovieListResponse(movies, movies.size(), 1, 0, movies.size());
            return Response.ok(response).build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
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
        if (movieOperator.getName().equals(compareOperator.getName())) {
            return true;
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