package com.movie.api.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "averageResponse")
public class AverageResponse {
    
    @XmlElement
    private Double averageLength;

    public AverageResponse() {}

    public AverageResponse(Double averageLength) {
        this.averageLength = averageLength;
    }

    // Getters and setters
    public Double getAverageLength() {
        return averageLength;
    }

    public void setAverageLength(Double averageLength) {
        this.averageLength = averageLength;
    }
}