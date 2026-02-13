package com.lab2.movie.application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.ws.Endpoint;
import com.lab2.movie.soap.MovieServiceImpl;

public class SoapContextListener implements ServletContextListener {
    
    private Endpoint endpoint;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // No endpoint publishing here - let web.xml handle servlet mapping
        System.out.println("Movie SOAP Service context initialized");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (endpoint != null) {
            endpoint.stop();
            System.out.println("Movie SOAP Service stopped");
        }
    }
}