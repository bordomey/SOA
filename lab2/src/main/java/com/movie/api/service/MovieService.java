package com.movie.api.service;

import com.movie.api.model.Movie;
import com.movie.api.model.Person;
import com.movie.api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    
    @Autowired
    private MovieRepository movieRepository;
    
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    
    public Page<Movie> getMoviesWithFiltering(String name, Integer oscarsCount, Long goldenPalmCount,
                                              Integer length, String genre, String operatorName,
                                              Float coordinateX, Double coordinateY,
                                              int page, int size, String sort) {
        // For simplicity, we'll implement basic filtering here
        // In a real application, you would use Specifications or QueryDSL for dynamic filtering
        Pageable pageable = PageRequest.of(page, size);
        if (sort != null && !sort.isEmpty()) {
            String[] sortParts = sort.split(",");
            if (sortParts.length == 2) {
                Sort.Direction direction = Sort.Direction.fromString(sortParts[1]);
                pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));
            }
        }
        return movieRepository.findAll(pageable);
    }
    
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }
    
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }
    
    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        
        movie.setName(movieDetails.getName());
        movie.setCoordinates(movieDetails.getCoordinates());
        movie.setOscarsCount(movieDetails.getOscarsCount());
        movie.setGoldenPalmCount(movieDetails.getGoldenPalmCount());
        movie.setLength(movieDetails.getLength());
        movie.setGenre(movieDetails.getGenre());
        movie.setOperator(movieDetails.getOperator());
        
        return movieRepository.save(movie);
    }
    
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        movieRepository.delete(movie);
    }
    
    public Double calculateAverageLength() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            return 0.0;
        }
        
        int totalLength = movies.stream()
                .mapToInt(Movie::getLength)
                .sum();
        
        return (double) totalLength / movies.size();
    }
    
    public Long countMoviesWithOperatorGreaterThan(String operatorName, 
                                                   java.time.LocalDate operatorBirthday,
                                                   Long operatorHeight) {
        return movieRepository.countMoviesWithOperatorGreaterThan(operatorName, operatorBirthday, operatorHeight);
    }
    
    public List<Movie> getMoviesWithOperatorGreaterThan(String operatorName,
                                                        java.time.LocalDate operatorBirthday,
                                                        Long operatorHeight) {
        return movieRepository.findMoviesWithOperatorGreaterThan(operatorName, operatorBirthday, operatorHeight);
    }
    
    public List<Movie> getMoviesByLengthGreaterThan(Integer minLength) {
        return movieRepository.findByLengthGreaterThan(minLength);
    }
}