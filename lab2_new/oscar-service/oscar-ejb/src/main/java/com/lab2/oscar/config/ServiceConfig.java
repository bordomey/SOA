package com.lab2.oscar.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ServiceConfig {
    private static final Logger logger = Logger.getLogger(ServiceConfig.class.getName());
    private static ServiceConfig instance;
    private Properties properties;

    private ServiceConfig() {
        loadProperties();
    }

    public static synchronized ServiceConfig getInstance() {
        if (instance == null) {
            instance = new ServiceConfig();
        }
        return instance;
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.warning("Unable to find application.properties file, using default values");
                // Set default values
                properties.setProperty("movie.service.base.url", "https://haproxy:8080/movie-service/api");
            } else {
                properties.load(input);
                logger.info("Loaded application.properties successfully");
            }
        } catch (IOException ex) {
            logger.severe("Error loading application.properties: " + ex.getMessage());
            // Set default values as fallback
            properties.setProperty("movie.service.base.url", "https://haproxy:8080/movie-service/api");
        }
    }

    public String getMovieServiceBaseUrl() {
        return properties.getProperty("movie.service.base.url", "https://haproxy:8080/movie-service/api");
    }
}