package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.exception.ScreeningAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.service.exception.BreakTimeException;
import com.epam.training.ticketservice.service.exception.OverlappingScreeningException;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {
    void createScreening(String movieTitle, String roomName, LocalDateTime screeningDateTime)
            throws ScreeningAlreadyExistsException,
            MovieNotFoundException, RoomNotFoundException, OverlappingScreeningException,
            BreakTimeException;

    void deleteScreening(String movieTitle, String roomName, LocalDateTime dateTime) throws ScreeningNotFoundException;

    List<Screening> getAllScreenings();
}
