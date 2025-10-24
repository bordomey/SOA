package com.movie.api.dto;

import com.movie.api.model.Coordinates;
import com.movie.api.model.MovieGenre;
import com.movie.api.model.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "movieRequest")
public class MovieRequest {
    
    @NotBlank
    @XmlElement(required = true)
    private String name;
    
    @NotNull
    @XmlElement(required = true)
    private Coordinates coordinates;
    
    @Positive
    @XmlElement(required = true)
    private Integer oscarsCount;
    
    @Positive
    @XmlElement(required = true)
    private Long goldenPalmCount;
    
    @Positive
    @XmlElement(required = true)
    private Integer length;
    
    @XmlElement
    private MovieGenre genre;
    
    @NotNull
    @XmlElement(required = true)
    private Person operator;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Integer getOscarsCount() {
        return oscarsCount;
    }

    public void setOscarsCount(Integer oscarsCount) {
        this.oscarsCount = oscarsCount;
    }

    public Long getGoldenPalmCount() {
        return goldenPalmCount;
    }

    public void setGoldenPalmCount(Long goldenPalmCount) {
        this.goldenPalmCount = goldenPalmCount;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public void setGenre(MovieGenre genre) {
        this.genre = genre;
    }

    public Person getOperator() {
        return operator;
    }

    public void setOperator(Person operator) {
        this.operator = operator;
    }
}