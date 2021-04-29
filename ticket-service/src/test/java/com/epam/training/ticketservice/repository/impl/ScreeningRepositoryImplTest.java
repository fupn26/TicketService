package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.ScreeningDao;
import com.epam.training.ticketservice.dataccess.entity.MovieEntity;
import com.epam.training.ticketservice.dataccess.entity.PriceComponentEntity;
import com.epam.training.ticketservice.dataccess.entity.RoomEntity;
import com.epam.training.ticketservice.dataccess.entity.ScreeningEntity;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.exception.ScreeningAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.ScreeningMalformedException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.repository.mapper.ScreeningMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith({MockitoExtension.class})
class ScreeningRepositoryImplTest {

    @InjectMocks
    private ScreeningRepositoryImpl screeningRepository;
    @Mock
    private ScreeningDao screeningDao;
    @Mock
    private ScreeningMapper screeningMapper;


    private static final String MOVIE_TITLE = "best movie";
    private static final String MOVIE_GENRE = "drama";
    private static final int MOVIE_LENGTH = 100;
    private static final String ROOM_NAME = "best room";
    private static final int ROWS = 10;
    private static final int COLUMNS = 20;
    private static final LocalDateTime TIME = LocalDateTime.now();

    private static final String PRICE_COMPONENT_NAME = "glamour";
    private static final int PRICE_COMPONENT_VALUE = -200;

    private static final PriceComponent PRICE_COMPONENT = new PriceComponent(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponent> PRICE_COMPONENTS = Set.of(PRICE_COMPONENT);
    private static final PriceComponentEntity PRICE_COMPONENT_ENTITY = new PriceComponentEntity(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponentEntity> PRICE_COMPONENT_ENTITIES = Set.of(PRICE_COMPONENT_ENTITY);

    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, PRICE_COMPONENTS);
    private static final MovieEntity MOVIE_ENTITY = new MovieEntity(MOVIE_TITLE, MOVIE_GENRE,
            MOVIE_LENGTH, PRICE_COMPONENT_ENTITIES);

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(ScreeningRepositoryImplTest.MOVIE_TITLE, genre, length, priceComponents);
        } catch (InvalidMovieLengthException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final Room ROOM = createRoom(ROOM_NAME, ROWS, COLUMNS, PRICE_COMPONENTS);
    private static final RoomEntity ROOM_ENTITY = new RoomEntity(ROOM_NAME, ROWS, COLUMNS, PRICE_COMPONENT_ENTITIES);

    private static Room createRoom(String roomName, int rows, int columns, Set<PriceComponent> priceComponents) {
        Room result = null;
        try {
            result = new Room(roomName, rows, columns, priceComponents);
        } catch (InvalidRowException | InvalidColumnException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final Screening SCREENING = new Screening(MOVIE, ROOM, TIME, PRICE_COMPONENTS);
    private static final ScreeningEntity SCREENING_ENTITY = new ScreeningEntity(MOVIE_ENTITY, ROOM_ENTITY,
            TIME, PRICE_COMPONENT_ENTITIES);
    private static final List<Screening> SCREENINGS = List.of(SCREENING, SCREENING, SCREENING);
    private static final List<ScreeningEntity> SCREENING_ENTITIES = List.of(SCREENING_ENTITY,
            SCREENING_ENTITY, SCREENING_ENTITY);


    @Test
    void testCreateScreeningWithoutError() throws ScreeningAlreadyExistsException {
        //Given
        when(screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(screeningMapper.mapToScreeningEntity(any())).thenReturn(SCREENING_ENTITY);

        //When
        screeningRepository.createScreening(SCREENING);

        //Then
        verify(screeningDao, times(1)).save(SCREENING_ENTITY);
    }

    @Test
    void testCreateScreeningWithScreeningAlreadyExistsException() {
        //Given
        when(screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(any(), any(), any()))
                .thenReturn(Optional.of(SCREENING_ENTITY));

        //Then
        assertThrows(ScreeningAlreadyExistsException.class, () -> {
            //When
            screeningRepository.createScreening(SCREENING);
        });
    }

    @Test
    void testGetAllScreeningsWithoutError() throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(screeningDao.findAll()).thenReturn(SCREENING_ENTITIES);
        when(screeningMapper.mapToScreening(any())).thenReturn(SCREENING);

        //When
        List<Screening> actual = screeningRepository.getAllScreenings();

        //Then
        assertThat(actual, equalTo(SCREENINGS));
    }

    @Test
    void testGetAllScreeningsWithMappingThrowInvalidColumnException() throws InvalidColumnException,
            InvalidMovieLengthException, InvalidRowException {
        //Given
        when(screeningDao.findAll()).thenReturn(SCREENING_ENTITIES);
        when(screeningMapper.mapToScreening(any())).thenThrow(new InvalidColumnException(""));

        //When
        List<Screening> actual = screeningRepository.getAllScreenings();

        //Then
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void testGetAllScreeningsWithMappingThrowInvalidRowException() throws InvalidColumnException,
            InvalidMovieLengthException, InvalidRowException {
        //Given
        when(screeningDao.findAll()).thenReturn(SCREENING_ENTITIES);
        when(screeningMapper.mapToScreening(any())).thenThrow(new InvalidRowException(""));

        //When
        List<Screening> actual = screeningRepository.getAllScreenings();

        //Then
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void testGetAllScreeningsWithMappingThrowInvalidMovieLengthException() throws InvalidColumnException,
            InvalidMovieLengthException, InvalidRowException {
        //Given
        when(screeningDao.findAll()).thenReturn(SCREENING_ENTITIES);
        when(screeningMapper.mapToScreening(any())).thenThrow(new InvalidMovieLengthException(""));

        //When
        List<Screening> actual = screeningRepository.getAllScreenings();

        //Then
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void testGetScreeningByMovieTitleRoomNameDateWithoutError() throws InvalidColumnException,
            InvalidMovieLengthException, InvalidRowException, ScreeningMalformedException, ScreeningNotFoundException {
        //Given
        when(screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(any(), any(), any()))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenReturn(SCREENING);

        //When
        Screening actual = screeningRepository.getScreeningByMovieTitleRoomNameDate(MOVIE_TITLE, ROOM_NAME, TIME);

        //Then
        assertThat(actual, equalTo(SCREENING));
    }

    @Test
    void testGetScreeningByMovieTitleRoomNameDateWithScreeningNotFoundException() {
        //Given
        when(screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(any(), any(), any()))
                .thenReturn(Optional.empty());

        //Then
        assertThrows(ScreeningNotFoundException.class, () -> {
            //When
            screeningRepository.getScreeningByMovieTitleRoomNameDate(MOVIE_TITLE, ROOM_NAME, TIME);
        });
    }

    @Test
    void testGetScreeningByMovieTitleRoomNameDateWithInvalidColumnExceptionThrowScreeningMalformedException()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(any(), any(), any()))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenThrow(new InvalidColumnException(""));

        //Then
        assertThrows(ScreeningNotFoundException.class, () -> {
            //When
            screeningRepository.getScreeningByMovieTitleRoomNameDate(MOVIE_TITLE, ROOM_NAME, TIME);
        });
    }

    @Test
    void testGetScreeningByMovieTitleRoomNameDateWithInvalidRowExceptionThrowScreeningMalformedException()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(any(), any(), any()))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenThrow(new InvalidRowException(""));

        //Then
        assertThrows(ScreeningNotFoundException.class, () -> {
            //When
            screeningRepository.getScreeningByMovieTitleRoomNameDate(MOVIE_TITLE, ROOM_NAME, TIME);
        });
    }

    @Test
    void testGetScreeningByMovieTitleRoomNameDateWithInvalidMovieLengthExceptionThrowScreeningMalformedException()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(any(), any(), any()))
                .thenReturn(Optional.of(SCREENING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenThrow(new InvalidMovieLengthException(""));

        //Then
        assertThrows(ScreeningNotFoundException.class, () -> {
            //When
            screeningRepository.getScreeningByMovieTitleRoomNameDate(MOVIE_TITLE, ROOM_NAME, TIME);
        });
    }

    @Test
    void testDeleteScreeningByMovieTitleRoomNameDateWithoutError() throws ScreeningNotFoundException {
        //Given
        when(screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(any(), any(), any()))
                .thenReturn(Optional.of(SCREENING_ENTITY));

        //When
        screeningRepository.deleteScreeningByMovieTitleRoomNameDate(MOVIE_TITLE, ROOM_NAME, TIME);

        //Then
        verify(screeningDao, times(1)).deleteByMovie_TitleAndRoom_NameAndDateTime(
                MOVIE_TITLE,
                ROOM_NAME,
                TIME
        );
    }

    @Test
    void testDeleteScreeningByMovieTitleRoomNameDateWithScreeningNotFoundException() {
        //Given
        when(screeningDao.findByMovie_TitleAndRoom_NameAndDateTime(any(), any(), any()))
                .thenReturn(Optional.empty());

        //Then
        assertThrows(ScreeningNotFoundException.class, () -> {
            //When
            screeningRepository.deleteScreeningByMovieTitleRoomNameDate(MOVIE_TITLE, ROOM_NAME, TIME);
        });
    }

    @Test
    void testDeleteAllScreeningsByMovieNameWithoutError() {
        //When
        screeningRepository.deleteAllScreeningsByMovieName(MOVIE_TITLE);

        //Then
        verify(screeningDao, times(1)).deleteAllByMovie_Title(MOVIE_TITLE);
    }

    @Test
    void testDeleteAllScreeningsByRoomNameWithoutError() {
        //When
        screeningRepository.deleteAllScreeningsByRoomName(ROOM_NAME);

        //Then
        verify(screeningDao, times(1)).deleteAllByRoom_Name(ROOM_NAME);
    }
}