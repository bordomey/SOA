package com.lab2.oscar.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "coordinates")
public class Coordinates {
    private Float x;
    private double y;

    public Coordinates() {}

    public Coordinates(Float x, double y) {
        this.x = x;
        this.y = y;
    }

    @XmlElement
    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    @XmlElement
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}