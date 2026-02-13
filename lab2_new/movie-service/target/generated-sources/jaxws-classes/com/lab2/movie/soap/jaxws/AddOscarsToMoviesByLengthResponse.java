
package com.lab2.movie.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.lab2.movie.model.OscarUpdateResponse;

@XmlRootElement(name = "addOscarsToMoviesByLengthResponse", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addOscarsToMoviesByLengthResponse", namespace = "http://soap.movie.lab2.com/")
public class AddOscarsToMoviesByLengthResponse {

    @XmlElement(name = "oscarUpdateResponse", namespace = "")
    private OscarUpdateResponse oscarUpdateResponse;

    /**
     * 
     * @return
     *     returns OscarUpdateResponse
     */
    public OscarUpdateResponse getOscarUpdateResponse() {
        return this.oscarUpdateResponse;
    }

    /**
     * 
     * @param oscarUpdateResponse
     *     the value for the oscarUpdateResponse property
     */
    public void setOscarUpdateResponse(OscarUpdateResponse oscarUpdateResponse) {
        this.oscarUpdateResponse = oscarUpdateResponse;
    }

}
