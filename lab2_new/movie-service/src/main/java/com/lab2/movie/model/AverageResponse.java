package com.lab2.movie.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "averageResponse")
public class AverageResponse {
    private double averageLength;

    public AverageResponse() {}

    public AverageResponse(double averageLength) {
        this.averageLength = averageLength;
    }

    @XmlElement
    public double getAverageLength() {
        return averageLength;
    }

    public void setAverageLength(double averageLength) {
        this.averageLength = averageLength;
    }
}