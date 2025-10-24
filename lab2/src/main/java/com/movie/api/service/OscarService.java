package com.movie.api.service;

import com.movie.api.model.Movie;
import com.movie.api.model.Person;
import com.movie.api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OscarService {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private MovieService movieService;
    
    public List<Person> getScreenwritersWithNoOscarWins() {
        // Get all movies
        List<Movie> allMovies = movieService.getAllMovies();
        
        // Extract operators (screenwriters) from movies with no Oscars
        return allMovies.stream()
                .filter(movie -> movie.getOscarsCount() == null || movie.getOscarsCount() == 0)
                .map(Movie::getOperator)
                .distinct()
                .collect(Collectors.toList());
    }
    
    public int addOscarsToMoviesByLength(int minLength, int oscarsToAdd) {
        List<Movie> qualifyingMovies = movieService.getMoviesByLengthGreaterThan(minLength);
        
        for (Movie movie : qualifyingMovies) {
            int currentOscars = movie.getOscarsCount() != null ? movie.getOscarsCount() : 0;
            movie.setOscarsCount(currentOscars + oscarsToAdd);
            movieRepository.save(movie);
        }
        
        return qualifyingMovies.size();
    }
}