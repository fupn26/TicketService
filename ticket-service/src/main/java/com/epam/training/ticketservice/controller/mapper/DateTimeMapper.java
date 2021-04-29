package com.epam.training.ticketservice.controller.mapper;

import java.time.LocalDateTime;

public interface DateTimeMapper {
    String mapToString(LocalDateTime dateTime, String format);

    LocalDateTime mapToLocalDateTime(String dateTime, String format);
}
