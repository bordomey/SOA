package com.lab2.oscar.service;

import com.lab2.oscar.model.Movie;
import com.lab2.oscar.model.MovieListResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.ArrayList;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import javax.ws.rs.ProcessingException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MovieServiceClient {
    private static final String MOVIE_SERVICE_BASE_URL = "https://localhost:9192/movie-service/api";
    private static final Logger logger = Logger.getLogger(MovieServiceClient.class.getName());
    private Client client;

    public MovieServiceClient() {
        logger.info("Initializing MovieServiceClient");
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            
            // Create the client with the all-trusting trust manager
            this.client = ClientBuilder.newBuilder()
                .sslContext(sc)
                .build();
            logger.info("MovieServiceClient initialized successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create SSL context: " + e.getMessage(), e);
            // Fallback to default client if SSL setup fails
            this.client = ClientBuilder.newClient();
        }
    }

    public List<Movie> getAllMovies() {
        logger.info("Attempting to get all movies from " + MOVIE_SERVICE_BASE_URL + "/movies");
        try {
            WebTarget target = client.target(MOVIE_SERVICE_BASE_URL + "/movies");
            logger.info("Created WebTarget for movies endpoint");
            
            Response response = target.request(MediaType.APPLICATION_XML)
                .get();
            logger.info("Received response with status: " + response.getStatus());
            
            if (response.getStatus() == 200) {
                logger.info("Processing successful response");
                // Parse the response to get the list of movies
                MovieListResponse movieListResponse = response.readEntity(MovieListResponse.class);
                logger.info("Successfully parsed movie list response, got " + movieListResponse.getMovies().size() + " movies");
                return movieListResponse.getMovies();
            } else {
                String errorResponse = response.readEntity(String.class);
                logger.severe("Error getting movies: " + response.getStatus() + " - " + errorResponse);
                return new ArrayList<>();
            }
        } catch (ProcessingException e) {
            logger.log(Level.SEVERE, "Network error while getting movies: " + e.getMessage(), e);
            return new ArrayList<>();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while getting movies: " + e.getMessage(), e);
            return new ArrayList<>();
        } finally {
            if (client != null) {
                try {
                    client.close();
                    logger.info("Client closed");
                } catch (Exception e) {
                    logger.warning("Error closing client: " + e.getMessage());
                }
            }
        }
    }

    public void updateMovie(long id, Movie movie) {
        logger.info("Attempting to update movie with ID: " + id);
        Client localClient = null;
        try {
            // Create a new client for this operation
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            
            localClient = ClientBuilder.newBuilder()
                .sslContext(sc)
                .build();
            logger.info("Created local client for update operation");

            WebTarget target = localClient.target(MOVIE_SERVICE_BASE_URL + "/movies/" + id);
            logger.info("Created WebTarget for update endpoint: " + target.getUri());
            
            Response response = target.request(MediaType.APPLICATION_XML)
                .put(Entity.xml(movie));
            logger.info("Response status: " + response.getStatus());
            if (response.getStatus() != 200) {
                String errorResponse = response.readEntity(String.class);
                logger.severe("Error updating movie: " + response.getStatus() + " - " + errorResponse);
            } else {
                logger.info("Successfully updated movie with ID: " + id);
            }
            response.close();
        } catch (ProcessingException e) {
            logger.log(Level.SEVERE, "Network error while updating movie: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while updating movie: " + e.getMessage(), e);
        } finally {
            if (localClient != null) {
                try {
                    localClient.close();
                    logger.info("Local client closed");
                } catch (Exception e) {
                    logger.warning("Error closing local client: " + e.getMessage());
                }
            }
        }
    }

    public void addOscarsToMoviesByLength(int minLength, int oscarsToAdd) {
        logger.info("Attempting to add " + oscarsToAdd + " Oscars to movies with length > " + minLength);
        Client localClient = null;
        try {
            // Create a new client for this operation
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            
            localClient = ClientBuilder.newBuilder()
                .sslContext(sc)
                .build();
            logger.info("Created local client for Oscars operation");

            WebTarget target = localClient.target(MOVIE_SERVICE_BASE_URL + 
                "/movies/honor-by-length/" + minLength + "/oscars-to-add")
                .queryParam("oscars-to-add", oscarsToAdd);
            logger.info("Created WebTarget for Oscars endpoint: " + target.getUri());
            
            Response response = target.request(MediaType.APPLICATION_XML)
                .method("PATCH", Entity.text(""));
            logger.info("Response status: " + response.getStatus());
            if (response.getStatus() != 200) {
                String errorResponse = response.readEntity(String.class);
                logger.severe("Error adding Oscars: " + response.getStatus() + " - " + errorResponse);
            } else {
                logger.info("Successfully added Oscars to movies");
            }
            response.close();
        } catch (ProcessingException e) {
            logger.log(Level.SEVERE, "Network error while adding Oscars: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while adding Oscars: " + e.getMessage(), e);
        } finally {
            if (localClient != null) {
                try {
                    localClient.close();
                    logger.info("Local client closed");
                } catch (Exception e) {
                    logger.warning("Error closing local client: " + e.getMessage());
                }
            }
        }
    }

    public void close() {
        logger.info("Closing MovieServiceClient");
        if (client != null) {
            try {
                client.close();
                logger.info("Client closed successfully");
            } catch (Exception e) {
                logger.warning("Error closing client: " + e.getMessage());
            }
        }
    }
}