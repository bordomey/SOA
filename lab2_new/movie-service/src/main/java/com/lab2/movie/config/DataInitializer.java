package com.lab2.movie.config;

import com.lab2.movie.dao.MovieDAO;
import com.lab2.movie.model.Coordinates;
import com.lab2.movie.model.Movie;
import com.lab2.movie.model.MovieGenre;
import com.lab2.movie.model.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize with some sample data
        MovieDAO movieDAO = MovieDAO.getInstance();
        
        // Create sample movies
        Movie movie1 = new Movie();
        movie1.setName("The Godfather");
        movie1.setOscarsCount(0);
        movie1.setGoldenPalmCount(0L);
        movie1.setLength(175);
        movie1.setGenre(MovieGenre.ADVENTURE);
        
        Coordinates coords1 = new Coordinates();
        coords1.setX(10.5f);
        coords1.setY(20.3);
        movie1.setCoordinates(coords1);
        
        Person director1 = new Person();
        director1.setName("Francis Ford Coppola");
        director1.setHeight(180L);
        movie1.setOperator(director1);
        movie1.setCreationDate(ZonedDateTime.now());
        
        movieDAO.createMovie(movie1);
        
        Movie movie2 = new Movie();
        movie2.setName("Pulp Fiction");
        movie2.setOscarsCount(1);
        movie2.setGoldenPalmCount(0L);
        movie2.setLength(154);
        movie2.setGenre(MovieGenre.WESTERN);
        
        Coordinates coords2 = new Coordinates();
        coords2.setX(15.2f);
        coords2.setY(25.7);
        movie2.setCoordinates(coords2);
        
        Person director2 = new Person();
        director2.setName("Quentin Tarantino");
        director2.setHeight(185L);
        movie2.setOperator(director2);
        movie2.setCreationDate(ZonedDateTime.now());
        
        movieDAO.createMovie(movie2);
        
        System.out.println("Sample data initialized successfully!");
    }
}