package com.lab2.movie.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "countResponse")
public class CountResponse {
    private long count;

    public CountResponse() {}

    public CountResponse(long count) {
        this.count = count;
    }

    @XmlElement
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}