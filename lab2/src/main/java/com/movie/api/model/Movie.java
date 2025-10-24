package com.movie.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

@Entity
@Table(name = "movies")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "movie")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(required = true)
    private Long id;
    
    @NotBlank
    @XmlElement(required = true)
    private String name;
    
    @Embedded
    @NotNull
    @XmlElement(required = true)
    private Coordinates coordinates;
    
    @Column(name = "creation_date")
    @XmlElement(required = true)
    private ZonedDateTime creationDate;
    
    @Positive
    @Column(name = "oscars_count")
    @XmlElement(required = true)
    private Integer oscarsCount;
    
    @Positive
    @Column(name = "golden_palm_count")
    @XmlElement(required = true)
    private Long goldenPalmCount;
    
    @Positive
    @XmlElement(required = true)
    private Integer length;
    
    @Enumerated(EnumType.STRING)
    @XmlElement
    private MovieGenre genre;
    
    @Embedded
    @NotNull
    @XmlElement(required = true)
    private Person operator;

    public Movie() {
        this.creationDate = ZonedDateTime.now();
    }

    public Movie(String name, Coordinates coordinates, Integer oscarsCount, 
                 Long goldenPalmCount, Integer length, MovieGenre genre, Person operator) {
        this();
        this.name = name;
        this.coordinates = coordinates;
        this.oscarsCount = oscarsCount;
        this.goldenPalmCount = goldenPalmCount;
        this.length = length;
        this.genre = genre;
        this.operator = operator;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
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