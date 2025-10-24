package com.movie.api.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "errorResponse")
public class ErrorResponse {
    
    @XmlElement
    private String error;
    
    @XmlElement
    private String message;
    
    @XmlElement
    private ZonedDateTime timestamp;
    
    @XmlElement
    private Integer status;

    public ErrorResponse() {
        this.timestamp = ZonedDateTime.now();
    }

    public ErrorResponse(String error, String message, Integer status) {
        this();
        this.error = error;
        this.message = message;
        this.status = status;
    }

    // Getters and setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}