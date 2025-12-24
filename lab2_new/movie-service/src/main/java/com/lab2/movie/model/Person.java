package com.lab2.movie.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.ZonedDateTime;

@XmlRootElement(name = "person")
public class Person {
    private String name;
    private ZonedDateTime birthday;
    private Long height;

    public Person() {}

    public Person(String name, ZonedDateTime birthday, Long height) {
        this.name = name;
        this.birthday = birthday;
        this.height = height;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    public ZonedDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(ZonedDateTime birthday) {
        this.birthday = birthday;
    }

    @XmlElement
    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }
}