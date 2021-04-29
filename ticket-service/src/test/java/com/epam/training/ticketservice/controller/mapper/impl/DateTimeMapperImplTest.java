package com.epam.training.ticketservice.controller.mapper.impl;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class DateTimeMapperImplTest {

    private static final DateTimeMapperImpl DATE_TIME_MAPPER = new DateTimeMapperImpl();

    private static final String FORMAT = "yyyy.MM.dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FORMAT);
    private static final LocalDateTime DATE_TIME = LocalDateTime.now();
    private static final String DATE_TIME_STRING = DATE_TIME.format(DATE_TIME_FORMATTER);
    private static final String INVALID_DATE_TIME_STRING = "2020.JAN.21 12:30:00";

    @Test
    void testMapToStringWithValidFormat() {
        //When
        String actual = DATE_TIME_MAPPER.mapToString(DATE_TIME, FORMAT);

        //Then
        assertThat(actual, equalTo(DATE_TIME_STRING));
    }

    @Test
    void testMapToLocalDateTimeWithValidString() {
        //When
        LocalDateTime actual = DATE_TIME_MAPPER.mapToLocalDateTime(DATE_TIME_STRING, FORMAT);

        //Then
        assertThat(actual.getYear(), equalTo(DATE_TIME.getYear()));
        assertThat(actual.getMonth(), equalTo(DATE_TIME.getMonth()));
        assertThat(actual.getDayOfMonth(), equalTo(DATE_TIME.getDayOfMonth()));
        assertThat(actual.getHour(), equalTo(DATE_TIME.getHour()));
        assertThat(actual.getMinute(), equalTo(DATE_TIME.getMinute()));
        assertThat(actual.getSecond(), equalTo(DATE_TIME.getSecond()));
    }

    @Test
    void testMapToLocalDateTimeWithInvalidStringThrowsDateTimeParseException() {
        //Then
        assertThrows(DateTimeParseException.class, () -> {
            //When
            LocalDateTime actual = DATE_TIME_MAPPER.mapToLocalDateTime(INVALID_DATE_TIME_STRING, FORMAT);
        });
    }
}