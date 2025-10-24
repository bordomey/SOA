package com.movie.api.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "countResponse")
public class CountResponse {
    
    @XmlElement
    private Long count;

    public CountResponse() {}

    public CountResponse(Long count) {
        this.count = count;
    }

    // Getters and setters
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}