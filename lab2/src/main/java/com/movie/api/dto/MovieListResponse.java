package com.movie.api.dto;

import com.movie.api.model.Movie;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "movieListResponse")
public class MovieListResponse {
    
    @XmlElement(name = "movie")
    private List<Movie> movies;
    
    @XmlElement
    private Long totalElements;
    
    @XmlElement
    private Integer totalPages;
    
    @XmlElement
    private Integer currentPage;
    
    @XmlElement
    private Integer pageSize;

    public MovieListResponse() {}

    public MovieListResponse(List<Movie> movies, Long totalElements, Integer totalPages, 
                            Integer currentPage, Integer pageSize) {
        this.movies = movies;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    // Getters and setters
    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}