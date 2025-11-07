package com.lab2.movie.dao;

import com.lab2.movie.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.time.ZonedDateTime;

public class MovieDAO {
    private static MovieDAO instance;
    private Map<Long, Movie> movies = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    private MovieDAO() {}

    public static synchronized MovieDAO getInstance() {
        if (instance == null) {
            instance = new MovieDAO();
        }
        return instance;
    }

    public Movie createMovie(Movie movie) {
        long id = idGenerator.getAndIncrement();
        movie.setId(id);
        movie.setCreationDate(ZonedDateTime.now());
        movies.put(id, movie);
        return movie;
    }

    public Movie getMovieById(long id) {
        return movies.get(id);
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values());
    }

    public Movie updateMovie(long id, Movie movie) {
        if (movies.containsKey(id)) {
            movie.setId(id);
            // Keep the original creation date
            movie.setCreationDate(movies.get(id).getCreationDate());
            movies.put(id, movie);
            return movie;
        }
        return null;
    }

    public boolean deleteMovie(long id) {
        return movies.remove(id) != null;
    }

    public int getMoviesCount() {
        return movies.size();
    }

    public void addOscarsToMoviesByLength(int minLength, int oscarsToAdd) {
        movies.values().forEach(movie -> {
            if (movie.getLength() > minLength) {
                movie.setOscarsCount(movie.getOscarsCount() + oscarsToAdd);
            }
        });
    }
}