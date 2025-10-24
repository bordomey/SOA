package com.movie.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "coordinates")
public class Coordinates {
    
    @NotNull
    @Column(name = "coordinate_x")
    @XmlElement(required = true)
    private Float x;
    
    @Column(name = "coordinate_y")
    @XmlElement(required = true)
    private Double y;

    public Coordinates() {}

    public Coordinates(Float x, Double y) {
        this.x = x;
        this.y = y;
    }

    // Getters and setters
    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}