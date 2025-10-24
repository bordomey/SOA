package com.movie.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "person")
public class Person {
    
    @NotBlank
    @Column(name = "operator_name")
    @XmlElement(required = true)
    private String name;
    
    @Column(name = "operator_birthday")
    @XmlElement
    private LocalDate birthday;
    
    @Positive
    @Column(name = "operator_height")
    @XmlElement
    private Long height;

    public Person() {}

    public Person(String name, LocalDate birthday, Long height) {
        this.name = name;
        this.birthday = birthday;
        this.height = height;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }
    
    // Custom comparison method for "greater than" operations
    public boolean isGreaterThan(Person other) {
        if (other == null) return true;
        
        int nameComparison = this.name.compareTo(other.name);
        if (nameComparison > 0) return true;
        if (nameComparison < 0) return false;
        
        if (this.birthday != null && other.birthday != null) {
            int birthdayComparison = this.birthday.compareTo(other.birthday);
            if (birthdayComparison > 0) return true;
            if (birthdayComparison < 0) return false;
        } else if (this.birthday != null) {
            return true;
        } else if (other.birthday != null) {
            return false;
        }
        
        if (this.height != null && other.height != null) {
            return this.height > other.height;
        } else if (this.height != null) {
            return true;
        }
        
        return false;
    }
}