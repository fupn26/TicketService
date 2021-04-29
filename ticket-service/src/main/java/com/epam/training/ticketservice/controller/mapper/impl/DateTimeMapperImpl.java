package com.epam.training.ticketservice.controller.mapper.impl;

import com.epam.training.ticketservice.controller.mapper.DateTimeMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeMapperImpl implements DateTimeMapper {

    @Override
    public String mapToString(LocalDateTime dateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(dateTimeFormatter);
    }

    @Override
    public LocalDateTime mapToLocalDateTime(String dateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }
}
