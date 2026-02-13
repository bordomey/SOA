
package com.lab2.movie.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "deleteMovie", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteMovie", namespace = "http://soap.movie.lab2.com/")
public class DeleteMovie {

    @XmlElement(name = "id", namespace = "")
    private long id;

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

}
