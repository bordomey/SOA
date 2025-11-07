package com.lab2.oscar.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "screenwritersResponse")
public class ScreenwritersResponse {
    private List<Person> screenwriters;

    public ScreenwritersResponse() {}

    public ScreenwritersResponse(List<Person> screenwriters) {
        this.screenwriters = screenwriters;
    }

    @XmlElement
    public List<Person> getScreenwriters() {
        return screenwriters;
    }

    public void setScreenwriters(List<Person> screenwriters) {
        this.screenwriters = screenwriters;
    }
}