package com.lab2.movie.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

@XmlRootElement(name = "errorResponse")
public class ErrorResponse {
    private String error;
    private String message;
    private ZonedDateTime timestamp;
    private int status;

    public ErrorResponse() {}

    public ErrorResponse(String error, String message, ZonedDateTime timestamp, int status) {
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
    }

    @XmlElement
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @XmlElement
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlElement
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @XmlElement
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}