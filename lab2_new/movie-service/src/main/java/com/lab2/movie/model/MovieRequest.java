package com.lab2.movie.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MovieRequest")
public class MovieRequest {
    private String name;
    private Coordinates coordinates;
    private Integer oscarsCount;
    private Long goldenPalmCount;
    private int length;
    private MovieGenre genre;
    private Person operator;

    public MovieRequest() {}

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @XmlElement
    public Integer getOscarsCount() {
        return oscarsCount;
    }

    public void setOscarsCount(Integer oscarsCount) {
        this.oscarsCount = oscarsCount;
    }

    @XmlElement
    public Long getGoldenPalmCount() {
        return goldenPalmCount;
    }

    public void setGoldenPalmCount(Long goldenPalmCount) {
        this.goldenPalmCount = goldenPalmCount;
    }

    @XmlElement
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @XmlElement
    public MovieGenre getGenre() {
        return genre;
    }

    public void setGenre(MovieGenre genre) {
        this.genre = genre;
    }

    @XmlElement
    public Person getOperator() {
        return operator;
    }

    public void setOperator(Person operator) {
        this.operator = operator;
    }
}