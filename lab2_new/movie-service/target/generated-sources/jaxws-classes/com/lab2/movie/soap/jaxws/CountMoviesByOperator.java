
package com.lab2.movie.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "countMoviesByOperator", namespace = "http://soap.movie.lab2.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "countMoviesByOperator", namespace = "http://soap.movie.lab2.com/", propOrder = {
    "operatorName",
    "operatorBirthday",
    "operatorHeight"
})
public class CountMoviesByOperator {

    @XmlElement(name = "operatorName", namespace = "")
    private String operatorName;
    @XmlElement(name = "operatorBirthday", namespace = "")
    private String operatorBirthday;
    @XmlElement(name = "operatorHeight", namespace = "")
    private Long operatorHeight;

    /**
     * 
     * @return
     *     returns String
     */
    public String getOperatorName() {
        return this.operatorName;
    }

    /**
     * 
     * @param operatorName
     *     the value for the operatorName property
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getOperatorBirthday() {
        return this.operatorBirthday;
    }

    /**
     * 
     * @param operatorBirthday
     *     the value for the operatorBirthday property
     */
    public void setOperatorBirthday(String operatorBirthday) {
        this.operatorBirthday = operatorBirthday;
    }

    /**
     * 
     * @return
     *     returns Long
     */
    public Long getOperatorHeight() {
        return this.operatorHeight;
    }

    /**
     * 
     * @param operatorHeight
     *     the value for the operatorHeight property
     */
    public void setOperatorHeight(Long operatorHeight) {
        this.operatorHeight = operatorHeight;
    }

}
