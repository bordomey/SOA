package com.lab2.movie.resource;

import com.lab2.movie.dao.MovieDAO;
import com.lab2.movie.model.*;
import com.lab2.movie.util.ValidationUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/movies")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public class MovieResource {
    private MovieDAO movieDAO = MovieDAO.getInstance();

    @GET
    public Response getAllMovies(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sort") List<String> sort,
            @QueryParam("name") String name,
            @QueryParam("oscarsCount") Integer oscarsCount,
            @QueryParam("goldenPalmCount") Long goldenPalmCount,
            @QueryParam("length") Integer length,
            @QueryParam("genre") MovieGenre genre,
            @QueryParam("operator.name") String operatorName,
            @QueryParam("coordinates.x") Float x,
            @QueryParam("coordinates.y") Double y) {

        try {
            List<Movie> allMovies = movieDAO.getAllMovies();

            // Apply filters
            List<Movie> filteredMovies = allMovies.stream()
                    .filter(movie -> name == null || movie.getName().contains(name))
                    .filter(movie -> oscarsCount == null || movie.getOscarsCount().equals(oscarsCount))
                    .filter(movie -> goldenPalmCount == null || movie.getGoldenPalmCount().equals(goldenPalmCount))
                    .filter(movie -> length == null || movie.getLength() == length)
                    .filter(movie -> genre == null || movie.getGenre() == genre)
                    .filter(movie -> operatorName == null || movie.getOperator().getName().contains(operatorName))
                    .filter(movie -> x == null || (movie.getCoordinates().getX() != null && Math.abs(movie.getCoordinates().getX() - x) < 0.001))
                    .filter(movie -> y == null || Math.abs(movie.getCoordinates().getY() - y) < 0.001)
                    .collect(Collectors.toList());

            // Apply sorting
            if (sort != null && !sort.isEmpty()) {
                for (String sortParam : sort) {
                    String[] parts = sortParam.split(",");
                    String field = parts[0];
                    String direction = parts.length > 1 ? parts[1].toLowerCase() : "asc";

                    Comparator<Movie> comparator = null;
                    switch (field) {
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
            return Response.ok(response).build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    @POST
    public Response createMovie(Movie movie) {
        try {
            // Validate movie
            String validationError = ValidationUtil.validateMovie(movie);
            if (validationError != null) {
                ErrorResponse error = new ErrorResponse("Bad Request: " + validationError, validationError, java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            Movie createdMovie = movieDAO.createMovie(movie);
            return Response.status(Response.Status.CREATED).entity(createdMovie).build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getMovieById(@PathParam("id") long id) {
        try {
            if (id <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "ID must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            Movie movie = movieDAO.getMovieById(id);
            if (movie == null) {
                ErrorResponse error = new ErrorResponse("Not Found", "Movie not found with ID: " + id, java.time.ZonedDateTime.now(), 404);
                return Response.status(Response.Status.NOT_FOUND).entity(error).build();
            }

            return Response.ok(movie).build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateMovie(@PathParam("id") long id, Movie movie) {
        try {
            if (id <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "ID must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            // Validate movie
            String validationError = ValidationUtil.validateMovie(movie);
            if (validationError != null) {
                ErrorResponse error = new ErrorResponse("Bad Request", validationError, java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            Movie updatedMovie = movieDAO.updateMovie(id, movie);
            if (updatedMovie == null) {
                ErrorResponse error = new ErrorResponse("Not Found", "Movie not found with ID: " + id, java.time.ZonedDateTime.now(), 404);
                return Response.status(Response.Status.NOT_FOUND).entity(error).build();
            }

            return Response.ok(updatedMovie).build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") long id) {
        try {
            if (id <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "ID must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            boolean deleted = movieDAO.deleteMovie(id);
            if (!deleted) {
                ErrorResponse error = new ErrorResponse("Not Found", "Movie not found with ID: " + id, java.time.ZonedDateTime.now(), 404);
                return Response.status(Response.Status.NOT_FOUND).entity(error).build();
            }

            return Response.noContent().build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    @PATCH
    @Path("/honor-by-length/{min-length}/oscars-to-add")
    public Response addOscarsToMoviesByLength(
            @PathParam("min-length") int minLength,
            @QueryParam("oscars-to-add") int oscarsToAdd) {
        try {
            if (minLength <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Minimum length must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
            if (oscarsToAdd <= 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Oscars to add must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
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
            return Response.ok(response).build();
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
}
