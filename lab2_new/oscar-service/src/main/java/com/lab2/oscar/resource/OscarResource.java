package com.lab2.oscar.resource;

import com.lab2.oscar.model.*;
import com.lab2.oscar.service.MovieServiceClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

@Path("/oscar")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class OscarResource {
    private static final Logger logger = Logger.getLogger(OscarResource.class.getName());
    private MovieServiceClient movieServiceClient = new MovieServiceClient();

    @GET
    @Path("/screenwriters/get-loosers")
    public Response getScreenwritersWithNoOscarWins() {
        logger.info("Received request for screenwriters with no Oscar wins");
        try {
            // Get all movies from the movie service
            logger.info("Calling MovieServiceClient.getAllMovies()");
            List<Movie> allMovies = movieServiceClient.getAllMovies();
            logger.info("Received " + allMovies.size() + " movies from MovieService");
            
            // Find screenwriters whose movies have no Oscars
            List<Person> screenwriters = allMovies.stream()
                .filter(movie -> movie.getOscarsCount() == 0)
                .map(Movie::getOperator)
                .distinct()
                .collect(Collectors.toList());
            logger.info("Found " + screenwriters.size() + " screenwriters with no Oscar wins");
                
            ScreenwritersResponse response = new ScreenwritersResponse(screenwriters);
            
            logger.info("Returning response with " + screenwriters.size() + " screenwriters");
            return Response.ok(response).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request for screenwriters with no Oscar wins: " + e.getMessage(), e);
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    @PATCH
    @Path("/movies/honor-by-length/{min-length}/oscars-to-add")
    public Response addOscarsToMoviesByLength(
            @PathParam("min-length") int minLength,
            @QueryParam("oscars-to-add") int oscarsToAdd) {
        try {
            if (minLength < 0) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Minimum length must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            
            if (oscarsToAdd < 0) {
                logger.warning("Invalid oscarsToAdd parameter: " + oscarsToAdd);
                ErrorResponse error = new ErrorResponse("Bad Request", "Oscars to add must be greater than 0", java.time.ZonedDateTime.now(), 400);
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            // Get all movies using CRUD operations
            logger.info("Getting all movies from MovieService");
            List<Movie> allMovies = movieServiceClient.getAllMovies();
            
            // Filter movies by length and update Oscars count
            int moviesAffected = 0;
            for (Movie movie : allMovies) {
                if (movie.getLength() > minLength) {
                    movie.setOscarsCount(movie.getOscarsCount() + oscarsToAdd);
                    // Update the movie using CRUD operations
                    logger.info("Updating movie ID " + movie.getId() + " with new Oscars count: " + movie.getOscarsCount());
                    movieServiceClient.updateMovie(movie.getId(), movie);
                    moviesAffected++;
                }
            }
            
            OscarUpdateResponse response = new OscarUpdateResponse(moviesAffected, "Successfully added " + oscarsToAdd + " Oscars to " + moviesAffected + " qualifying movies");
            logger.info("Returning success response. Movies affected: " + moviesAffected);
            return Response.ok(response).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request to add Oscars: " + e.getMessage(), e);
            ErrorResponse error = new ErrorResponse("Internal Server Error", e.getMessage(), java.time.ZonedDateTime.now(), 500);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
}