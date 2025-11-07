package com.lab2.movie.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

@XmlRootElement(name = "MovieRequest")
public class Movie {
    private long id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private Integer oscarsCount;
    private Long goldenPalmCount;
    private int length;
    private MovieGenre genre;
    private Person operator;

    public Movie() {}

    public Movie(long id, String name, Coordinates coordinates, ZonedDateTime creationDate,
                 Integer oscarsCount, Long goldenPalmCount, int length, MovieGenre genre, Person operator) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.goldenPalmCount = goldenPalmCount;
        this.length = length;
        this.genre = genre;
        this.operator = operator;
    }

    @XmlElement
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
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