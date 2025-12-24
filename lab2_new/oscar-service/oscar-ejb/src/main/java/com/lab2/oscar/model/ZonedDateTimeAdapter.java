package com.lab2.oscar.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    public String marshal(ZonedDateTime zonedDateTime) throws Exception {
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.format(FORMATTER);
    }

    @Override
    public ZonedDateTime unmarshal(String dateString) throws Exception {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return ZonedDateTime.parse(dateString, FORMATTER);
    }
}