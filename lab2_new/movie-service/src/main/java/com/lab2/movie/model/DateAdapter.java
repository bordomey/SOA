package com.lab2.movie.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String, Date> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String marshal(Date date) throws Exception {
        return dateFormat.format(date);
    }

    @Override
    public Date unmarshal(String dateString) throws Exception {
        return dateFormat.parse(dateString);
    }
}