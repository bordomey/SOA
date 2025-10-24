package com.movie.api.repository;

import com.movie.api.model.Movie;
import com.movie.api.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    // Find movies by name (partial match)
    List<Movie> findByNameContainingIgnoreCase(String name);
    
    // Find movies by exact oscars count
    List<Movie> findByOscarsCount(Integer oscarsCount);
    
    // Find movies by exact golden palm count
    List<Movie> findByGoldenPalmCount(Long goldenPalmCount);
    
    // Find movies by exact length
    List<Movie> findByLength(Integer length);
    
    // Find movies by genre
    List<Movie> findByGenre(String genre);
    
    // Find movies by operator name (partial match)
    List<Movie> findByOperatorNameContainingIgnoreCase(String operatorName);
    
    // Find movies by coordinates
    List<Movie> findByCoordinatesX(Float x);
    List<Movie> findByCoordinatesY(Double y);
    
    // Custom query for movies with operator greater than specified
    @Query("SELECT m FROM Movie m WHERE " +
           "m.operator.name > :operatorName OR " +
           "(m.operator.name = :operatorName AND m.operator.birthday > :operatorBirthday) OR " +
           "(m.operator.name = :operatorName AND m.operator.birthday = :operatorBirthday AND m.operator.height > :operatorHeight)")
    List<Movie> findMoviesWithOperatorGreaterThan(
        @Param("operatorName") String operatorName,
        @Param("operatorBirthday") java.time.LocalDate operatorBirthday,
        @Param("operatorHeight") Long operatorHeight
    );
    
    // Count movies with operator greater than specified
    @Query("SELECT COUNT(m) FROM Movie m WHERE " +
           "m.operator.name > :operatorName OR " +
           "(m.operator.name = :operatorName AND m.operator.birthday > :operatorBirthday) OR " +
           "(m.operator.name = :operatorName AND m.operator.birthday = :operatorBirthday AND m.operator.height > :operatorHeight)")
    Long countMoviesWithOperatorGreaterThan(
        @Param("operatorName") String operatorName,
        @Param("operatorBirthday") java.time.LocalDate operatorBirthday,
        @Param("operatorHeight") Long operatorHeight
    );
    
    // Find movies with length greater than specified
    List<Movie> findByLengthGreaterThan(Integer minLength);
}