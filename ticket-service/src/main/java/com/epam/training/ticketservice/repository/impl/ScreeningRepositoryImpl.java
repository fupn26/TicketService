package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dao.ScreeningDao;
import com.epam.training.ticketservice.dao.entity.ScreeningEntity;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.repository.exception.ScreeningAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.ScreeningMalformedException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.repository.mapper.MovieMapper;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import com.epam.training.ticketservice.repository.mapper.RoomMapper;
import com.epam.training.ticketservice.repository.mapper.ScreeningMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScreeningRepositoryImpl implements ScreeningRepository {

    private final ScreeningDao screeningDao;
    private final ScreeningMapper screeningMapper;
    private final MovieMapper movieMapper;
    private final RoomMapper roomMapper;
    private final PriceComponentMapper priceComponentMapper;
    private final String SCREENING_NOT_FOUND = "Screening not found: <%s> <%s> <%s>";
    private final String SCREENING_ALREADY_EXISTS = "Screening already exists: %s";

    @Override
    public void createScreening(Screening screeningToCreate) throws ScreeningAlreadyExistsException {
        if (isScreeningExists(
                screeningToCreate.getMovie().getTitle(),
                screeningToCreate.getRoom().getName(),
                screeningToCreate.getStartDate()
        )) {
            throw new ScreeningAlreadyExistsException(String.format("Screening already exists: %s",
                    screeningToCreate));
        }
        screeningDao.save(screeningMapper.mapToScreeningEntity(screeningToCreate));
    }

    @Override
    public List<Screening> getAllScreenings() {
        return screeningDao.findAll().stream()
                .map(this::mapToScreening)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Screening getScreeningByMovieTitleRoomNameDate(String movieTitle,
                                                          String roomName, LocalDateTime screeningTime)
            throws ScreeningNotFoundException, ScreeningMalformedException {
        Optional<ScreeningEntity> screeningEntity = screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(movieTitle,
                roomName, screeningTime);
        if (screeningEntity.isEmpty()) {
            throw new ScreeningNotFoundException(String.format(SCREENING_NOT_FOUND,
                    movieTitle, roomName, mapToString(screeningTime)));
        }
        Optional<Screening> screening = mapToScreening(screeningEntity.get());
        if (screening.isEmpty()) {
            throw new ScreeningMalformedException(String.format(SCREENING_NOT_FOUND,
                    movieTitle, roomName, mapToString(screeningTime)));
        }
        return screening.get();
    }

    @Override
    public void deleteScreeningByMovieTitleRoomNameDate(String movieTitle,
                                                        String roomName, LocalDateTime screeningTime)
            throws ScreeningNotFoundException {
        if (!isScreeningExists(movieTitle, roomName, screeningTime)) {
            throw new ScreeningNotFoundException(String.format(SCREENING_NOT_FOUND,
                    movieTitle, roomName, mapToString(screeningTime)));
        }
        screeningDao.deleteByMovie_TitleAndRoom_NameAndDateTime(movieTitle, roomName, screeningTime);
    }

    @Override
    public void deleteScreeningsByMovieName(String movieTitle) {
        screeningDao.deleteAllByMovie_Title(movieTitle);
    }

    @Override
    public void deleteScreeningsByRoomName(String roomName) {
        screeningDao.deleteAllByRoom_Name(roomName);

    }

    private boolean isScreeningExists(String movieTitle, String roomName, LocalDateTime screeningTime) {
        return screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(
                movieTitle,
                roomName,
                screeningTime
        ).isPresent();
    }

    private Optional<Screening> mapToScreening(ScreeningEntity screeningEntityToMap) {
        Optional<Screening> result = Optional.empty();
        try {
             result = Optional.of(screeningMapper.mapToScreening(screeningEntityToMap));
        } catch (InvalidMovieLengthException | InvalidColumnException | InvalidRowException e) {
            log.warn(e.getMessage());
        }
        return result;
    }

    private String mapToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
