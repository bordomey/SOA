package com.lab2.oscar.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "oscarUpdateResponse")
public class OscarUpdateResponse {
    private int updatedMoviesCount;
    private String message;

    public OscarUpdateResponse() {}

    public OscarUpdateResponse(int updatedMoviesCount, String message) {
        this.updatedMoviesCount = updatedMoviesCount;
        this.message = message;
    }

    @XmlElement
    public int getUpdatedMoviesCount() {
        return updatedMoviesCount;
    }

    public void setUpdatedMoviesCount(int updatedMoviesCount) {
        this.updatedMoviesCount = updatedMoviesCount;
    }

    @XmlElement
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}