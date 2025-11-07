package com.lab2.movie.rest;

import com.lab2.movie.resource.MovieResource;
import com.lab2.movie.resource.MovieStatisticsResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(MovieResource.class);
        resources.add(MovieStatisticsResource.class);
        resources.add(CorsFilter.class);
        resources.add(CorsRequestFilter.class);
        return resources;
    }
}