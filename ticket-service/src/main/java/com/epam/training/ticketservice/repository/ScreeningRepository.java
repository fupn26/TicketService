package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.repository.exception.ScreeningAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.ScreeningMalformedException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository {
    void createScreening(Screening screeningToCreate) throws ScreeningAlreadyExistsException;

    List<Screening> getAllScreenings();

    Screening getScreeningByMovieTitleRoomNameDate(String movieTitle,
                                                   String roomName,
                                                   LocalDateTime screeningTime) throws ScreeningNotFoundException, ScreeningMalformedException;

    void deleteScreeningByMovieTitleRoomNameDate(String movieTitle,
                                                 String roomName,
                                                 LocalDateTime screeningTime) throws ScreeningNotFoundException;

    void deleteAllScreeningsByMovieName(String movieTitle);

    void deleteAllScreeningsByRoomName(String roomName);
}
