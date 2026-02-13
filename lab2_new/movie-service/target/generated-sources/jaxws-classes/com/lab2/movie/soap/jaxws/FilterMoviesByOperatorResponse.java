
package com.lab2.movie.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.lab2.movie.model.MovieListResponse;

@XmlRootElement(name = "filterMoviesByOperatorResponse", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filterMoviesByOperatorResponse", namespace = "http://soap.movie.lab2.com/")
public class FilterMoviesByOperatorResponse {

    @XmlElement(name = "movieListResponse", namespace = "")
    private MovieListResponse movieListResponse;

    /**
     * 
     * @return
     *     returns MovieListResponse
     */
    public MovieListResponse getMovieListResponse() {
        return this.movieListResponse;
    }

    /**
     * 
     * @param movieListResponse
     *     the value for the movieListResponse property
     */
    public void setMovieListResponse(MovieListResponse movieListResponse) {
        this.movieListResponse = movieListResponse;
    }

}
