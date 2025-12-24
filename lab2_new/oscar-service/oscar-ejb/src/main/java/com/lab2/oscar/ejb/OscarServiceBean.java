package com.lab2.oscar.ejb;

import com.lab2.oscar.model.*;
import com.lab2.oscar.service.MovieServiceClient;

import javax.ejb.Stateless;
import javax.ejb.Remote;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Stateless
@Remote(OscarServiceRemote.class)
public class OscarServiceBean implements OscarServiceRemote {
    private static final Logger logger = Logger.getLogger(OscarServiceBean.class.getName());
    private MovieServiceClient movieServiceClient = new MovieServiceClient();

    @Override
    public ScreenwritersResponse getScreenwritersWithNoOscarWins() {
        logger.info("Processing request for screenwriters with no Oscar wins");
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
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request for screenwriters with no Oscar wins: " + e.getMessage(), e);
            throw new RuntimeException("Internal Server Error", e);
        }
    }

    @Override
    public OscarUpdateResponse addOscarsToMoviesByLength(int minLength, int oscarsToAdd) {
        try {
            if (minLength < 0) {
                throw new IllegalArgumentException("Minimum length must be greater than 0");
            }
            
            if (oscarsToAdd < 0) {
                logger.warning("Invalid oscarsToAdd parameter: " + oscarsToAdd);
                throw new IllegalArgumentException("Oscars to add must be greater than 0");
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
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request to add Oscars: " + e.getMessage(), e);
            throw new RuntimeException("Internal Server Error", e);
        }
    }
}