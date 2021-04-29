package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.exception.ScreeningAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.service.ScreeningService;
import com.epam.training.ticketservice.service.exception.BreakTimeException;
import com.epam.training.ticketservice.service.exception.OverlappingScreeningException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    @Override
    public void createScreening(String movieTitle, String roomName, LocalDateTime screeningDateTime)
            throws ScreeningAlreadyExistsException, MovieNotFoundException,
            RoomNotFoundException, OverlappingScreeningException, BreakTimeException {
        Movie movie = getMovieByTitle(movieTitle);
        Room room = getRoomByName(roomName);
        if (areAnyOverlappingScreenings(screeningDateTime,
                screeningDateTime.plusMinutes(movie.getLength()), roomName)) {
            throw new OverlappingScreeningException("The screening is overlapping with other screening(s)");
        }
        if (areAnyOverlappingScreeningsWithBreak(screeningDateTime,
                screeningDateTime.plusMinutes(movie.getLength()), roomName)) {
            throw new BreakTimeException("The screening is overlapping with break time");
        }
        screeningRepository.createScreening(new Screening(movie, room, screeningDateTime));
    }

    @Override
    public void deleteScreening(String movieTitle, String roomName, LocalDateTime dateTime)
            throws ScreeningNotFoundException {
        screeningRepository.deleteScreeningByMovieTitleRoomNameDate(movieTitle, roomName, dateTime);
    }

    @Override
    public List<Screening> getAllScreenings() {
        return screeningRepository.getAllScreenings();
    }

    private Movie getMovieByTitle(String movieTitle) throws MovieNotFoundException {
        return movieRepository.getMovieByTitle(movieTitle);
    }

    private Room getRoomByName(String roomName) throws RoomNotFoundException {
        return roomRepository.getRoomByName(roomName);
    }

    private boolean areAnyOverlappingScreenings(LocalDateTime screeningStart,
                                                LocalDateTime screeningEnd,
                                                String roomName) {
        return screeningRepository.getAllScreenings().stream()
                .filter(screening -> screening.getRoom().getName().equals(roomName))
                .anyMatch(screening -> {
                    LocalDateTime currentScreeningStart = screening.getStartDate();
                    LocalDateTime currentScreeningEnd = screening.getStartDate()
                            .plusMinutes(screening.getMovie().getLength());
                    return isBetweenDatesInclusive(currentScreeningStart, currentScreeningEnd, screeningStart)
                            || isBetweenDatesInclusive(currentScreeningStart, currentScreeningEnd, screeningEnd);
                });
    }

    private boolean areAnyOverlappingScreeningsWithBreak(LocalDateTime screeningStart,
                                           LocalDateTime screeningEnd,
                                           String roomName) {
        return screeningRepository.getAllScreenings().stream()
                .filter(screening -> screening.getRoom().getName().equals(roomName))
                .anyMatch(screening -> {
                    LocalDateTime currentScreeningStart = screening.getStartDate().minusMinutes(10);
                    LocalDateTime currentScreeningEnd = screening.getStartDate()
                            .plusMinutes(screening.getMovie().getLength()).plusMinutes(10);
                    return isBetweenDatesExclusive(currentScreeningStart, currentScreeningEnd, screeningStart)
                            || isBetweenDatesExclusive(currentScreeningStart, currentScreeningEnd, screeningEnd);
                });
    }

    private boolean isBetweenDatesInclusive(LocalDateTime start, LocalDateTime end,
                                            LocalDateTime dateTimeToCheck) {
        return (dateTimeToCheck.isAfter(start) || dateTimeToCheck.isEqual(start))
                && (dateTimeToCheck.isBefore(end) || dateTimeToCheck.isEqual(end));
    }

    private boolean isBetweenDatesExclusive(LocalDateTime start, LocalDateTime end,
                                            LocalDateTime dateTimeToCheck) {
        return dateTimeToCheck.isAfter(start) && dateTimeToCheck.isBefore(end);
    }
}
