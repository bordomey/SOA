
package com.lab2.movie.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "addOscarsToMoviesByLength", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addOscarsToMoviesByLength", namespace = "http://soap.movie.lab2.com/", propOrder = {
    "minLength",
    "oscarsToAdd"
})
public class AddOscarsToMoviesByLength {

    @XmlElement(name = "minLength", namespace = "")
    private int minLength;
    @XmlElement(name = "oscarsToAdd", namespace = "")
    private int oscarsToAdd;

    /**
     * 
     * @return
     *     returns int
     */
    public int getMinLength() {
        return this.minLength;
    }

    /**
     * 
     * @param minLength
     *     the value for the minLength property
     */
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    /**
     * 
     * @return
     *     returns int
     */
    public int getOscarsToAdd() {
        return this.oscarsToAdd;
    }

    /**
     * 
     * @param oscarsToAdd
     *     the value for the oscarsToAdd property
     */
    public void setOscarsToAdd(int oscarsToAdd) {
        this.oscarsToAdd = oscarsToAdd;
    }

}
