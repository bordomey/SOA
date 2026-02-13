
package com.lab2.movie.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.lab2.movie.model.MovieRequest;

@XmlRootElement(name = "createMovie", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createMovie", namespace = "http://soap.movie.lab2.com/")
public class CreateMovie {

    @XmlElement(name = "movieRequest", namespace = "")
    private MovieRequest movieRequest;

    /**
     * 
     * @return
     *     returns MovieRequest
     */
    public MovieRequest getMovieRequest() {
        return this.movieRequest;
    }

    /**
     * 
     * @param movieRequest
     *     the value for the movieRequest property
     */
    public void setMovieRequest(MovieRequest movieRequest) {
        this.movieRequest = movieRequest;
    }

}
