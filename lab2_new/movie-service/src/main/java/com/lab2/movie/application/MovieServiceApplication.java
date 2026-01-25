package com.lab2.movie.application;

import javax.xml.ws.Endpoint;
import com.lab2.movie.soap.MovieServiceImpl;

public class MovieServiceApplication {
    public static void main(String[] args) {
        String url = "http://0.0.0.0:8080/MovieService";
        if (args.length > 0) {
            url = args[0];
        }
        
        Endpoint.publish(url, new MovieServiceImpl());
        System.out.println("Movie SOAP Service is running at: " + url);
        
        // Keep the application running
        synchronized (MovieServiceApplication.class) {
            try {
                MovieServiceApplication.class.wait();
            } catch (InterruptedException e) {
                System.out.println("Service interrupted");
            }
        }
    }
}