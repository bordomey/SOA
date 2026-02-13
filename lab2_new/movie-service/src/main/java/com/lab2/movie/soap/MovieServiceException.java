package com.lab2.movie.soap;

import javax.xml.ws.WebFault;

@WebFault(name = "MovieServiceFault", targetNamespace = "http://soap.movie.lab2.com/")
public class MovieServiceException extends Exception {
    private String errorCode;
    
    public MovieServiceException(String message) {
        super(message);
    }
    
    public MovieServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public MovieServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MovieServiceException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}