package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.exception.ScreeningAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.service.ScreeningService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    @Override
    public void createScreening(String movieTitle, String roomName, LocalDateTime dateTime)
            throws ScreeningAlreadyExistsException, MovieNotFoundException,
            RoomNotFoundException {

    }

    @Override
    public void deleteScreening(String movieTitle, String roomName, LocalDateTime dateTime)
            throws ScreeningNotFoundException {

    }

    @Override
    public List<Screening> getAllScreening() {
        return null;
    }
}
