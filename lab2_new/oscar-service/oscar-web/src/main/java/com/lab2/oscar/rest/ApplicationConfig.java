package com.lab2.oscar.rest;

import com.lab2.oscar.resource.OscarResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(OscarResource.class);
        resources.add(CorsFilter.class);
        resources.add(CorsRequestFilter.class);
        return resources;
    }
}