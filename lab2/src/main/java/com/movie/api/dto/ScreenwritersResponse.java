package com.movie.api.dto;

import com.movie.api.model.Person;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "screenwritersResponse")
public class ScreenwritersResponse {
    
    @XmlElement(name = "person")
    private List<Person> screenwriters;

    public ScreenwritersResponse() {}

    public ScreenwritersResponse(List<Person> screenwriters) {
        this.screenwriters = screenwriters;
    }

    // Getters and setters
    public List<Person> getScreenwriters() {
        return screenwriters;
    }

    public void setScreenwriters(List<Person> screenwriters) {
        this.screenwriters = screenwriters;
    }
}