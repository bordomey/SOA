package com.movie.api.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "oscarUpdateResponse")
public class OscarUpdateResponse {
    
    @XmlElement
    private Integer updatedMoviesCount;
    
    @XmlElement
    private String message;

    public OscarUpdateResponse() {}

    public OscarUpdateResponse(Integer updatedMoviesCount, String message) {
        this.updatedMoviesCount = updatedMoviesCount;
        this.message = message;
    }

    // Getters and setters
    public Integer getUpdatedMoviesCount() {
        return updatedMoviesCount;
    }

    public void setUpdatedMoviesCount(Integer updatedMoviesCount) {
        this.updatedMoviesCount = updatedMoviesCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}