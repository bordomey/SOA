
package com.lab2.movie.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getMovieByIdResponse", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMovieByIdResponse", namespace = "http://soap.movie.lab2.com/")
public class GetMovieByIdResponse {

    @XmlElement(name = "movie", namespace = "")
    private Object movie;

    /**
     * 
     * @return
     *     returns Object
     */
    public Object getMovie() {
        return this.movie;
    }

    /**
     * 
     * @param movie
     *     the value for the movie property
     */
    public void setMovie(Object movie) {
        this.movie = movie;
    }

}
