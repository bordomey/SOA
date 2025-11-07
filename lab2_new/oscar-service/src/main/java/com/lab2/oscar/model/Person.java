package com.lab2.oscar.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "person")
public class Person {
    private String name;
    private Date birthday;
    private Long height;

    public Person() {}

    public Person(String name, Date birthday, Long height) {
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
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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