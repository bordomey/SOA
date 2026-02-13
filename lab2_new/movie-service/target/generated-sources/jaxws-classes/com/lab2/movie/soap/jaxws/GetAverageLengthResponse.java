
package com.lab2.movie.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.lab2.movie.model.AverageResponse;

@XmlRootElement(name = "getAverageLengthResponse", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAverageLengthResponse", namespace = "http://soap.movie.lab2.com/")
public class GetAverageLengthResponse {

    @XmlElement(name = "averageResponse", namespace = "")
    private AverageResponse averageResponse;

    /**
     * 
     * @return
     *     returns AverageResponse
     */
    public AverageResponse getAverageResponse() {
        return this.averageResponse;
    }

    /**
     * 
     * @param averageResponse
     *     the value for the averageResponse property
     */
    public void setAverageResponse(AverageResponse averageResponse) {
        this.averageResponse = averageResponse;
    }

}
