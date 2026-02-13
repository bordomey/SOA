
package com.lab2.movie.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.lab2.movie.model.CountResponse;

@XmlRootElement(name = "countMoviesByOperatorResponse", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "countMoviesByOperatorResponse", namespace = "http://soap.movie.lab2.com/")
public class CountMoviesByOperatorResponse {

    @XmlElement(name = "countResponse", namespace = "")
    private CountResponse countResponse;

    /**
     * 
     * @return
     *     returns CountResponse
     */
    public CountResponse getCountResponse() {
        return this.countResponse;
    }

    /**
     * 
     * @param countResponse
     *     the value for the countResponse property
     */
    public void setCountResponse(CountResponse countResponse) {
        this.countResponse = countResponse;
    }

}
