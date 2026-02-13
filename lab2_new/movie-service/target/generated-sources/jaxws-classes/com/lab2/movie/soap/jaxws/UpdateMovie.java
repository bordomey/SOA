
package com.lab2.movie.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.lab2.movie.model.MovieRequest;

@XmlRootElement(name = "updateMovie", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateMovie", namespace = "http://soap.movie.lab2.com/", propOrder = {
    "id",
    "movieRequest"
})
public class UpdateMovie {

    @XmlElement(name = "id", namespace = "")
    private long id;
    @XmlElement(name = "movieRequest", namespace = "")
    private MovieRequest movieRequest;

    /**
     * 
     * @return
     *     returns long
     */
    public long getId() {
        return this.id;
    }

    /**
     * 
     * @param id
     *     the value for the id property
     */
    public void setId(long id) {
        this.id = id;
    }

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
