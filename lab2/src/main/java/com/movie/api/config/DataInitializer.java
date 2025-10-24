package com.movie.api.config;

import com.movie.api.model.Coordinates;
import com.movie.api.model.Movie;
import com.movie.api.model.MovieGenre;
import com.movie.api.model.Person;
import com.movie.api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Override
    public void run(String... args) throws Exception {
        try {
            // Create sample movies
            Movie movie1 = new Movie();
            movie1.setName("The Godfather");
            movie1.setCoordinates(new Coordinates(10.5f, 25.7));
            movie1.setCreationDate(ZonedDateTime.now());
            movie1.setOscarsCount(3);
            movie1.setGoldenPalmCount(1L);
            movie1.setLength(175);
            movie1.setGenre(MovieGenre.ADVENTURE);
            movie1.setOperator(new Person("Francis Ford Coppola", LocalDate.of(1939, 4, 7), 180L));
            
            Movie movie2 = new Movie();
            movie2.setName("Pulp Fiction");
            movie2.setCoordinates(new Coordinates(15.2f, 30.1));
            movie2.setCreationDate(ZonedDateTime.now());
            movie2.setOscarsCount(1);
            movie2.setGoldenPalmCount(1L);
            movie2.setLength(154);
            movie2.setGenre(MovieGenre.WESTERN);
            movie2.setOperator(new Person("Quentin Tarantino", LocalDate.of(1963, 3, 27), 185L));
            
            Movie movie3 = new Movie();
            movie3.setName("The Shawshank Redemption");
            movie3.setCoordinates(new Coordinates(8.7f, 22.3));
            movie3.setCreationDate(ZonedDateTime.now());
            movie3.setOscarsCount(7);
            movie3.setGoldenPalmCount(1L);
            movie3.setLength(142);
            movie3.setGenre(MovieGenre.SCIENCE_FICTION);
            movie3.setOperator(new Person("Frank Darabont", LocalDate.of(1959, 1, 28), 180L));
            
            movieRepository.save(movie1);
            movieRepository.save(movie2);
            movieRepository.save(movie3);
        } catch (Exception e) {
            System.out.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}