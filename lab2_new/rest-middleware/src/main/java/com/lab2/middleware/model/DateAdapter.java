package com.lab2.middleware.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateAdapter extends XmlAdapter<String, ZonedDateTime> {
    private DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String marshal(ZonedDateTime dateTime) throws Exception {
        return dateTime.format(fullFormatter);
    }

    @Override
    public ZonedDateTime unmarshal(String dateString) throws Exception {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Try full format first
            return ZonedDateTime.parse(dateString, fullFormatter);
        } catch (DateTimeParseException e) {
            // If that fails, try date-only format
            try {
                return java.time.LocalDate.parse(dateString, dateFormatter)
                    .atStartOfDay(java.time.ZoneId.systemDefault());
            } catch (DateTimeParseException e2) {
                // If both fail, rethrow the original exception
                throw e;
            }
        }
    }
}