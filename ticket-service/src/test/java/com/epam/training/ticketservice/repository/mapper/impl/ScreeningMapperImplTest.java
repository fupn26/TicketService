package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dao.entity.MovieEntity;
import com.epam.training.ticketservice.dao.entity.PriceComponentEntity;
import com.epam.training.ticketservice.dao.entity.RoomEntity;
import com.epam.training.ticketservice.dao.entity.ScreeningEntity;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.mapper.MovieMapper;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import com.epam.training.ticketservice.repository.mapper.RoomMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ScreeningMapperImplTest {

    @InjectMocks
    private ScreeningMapperImpl screeningMapper;
    @Mock
    private MovieMapper movieMapper;
    @Mock
    private RoomMapper roomMapper;
    @Mock
    private PriceComponentMapper priceComponentMapper;

    private static final String MOVIE_TITLE = "best movie";
    private static final String MOVIE_GENRE = "drama";
    private static final int INVALID_MOVIE_LENGTH = 0;
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
            result = new Movie(ScreeningMapperImplTest.MOVIE_TITLE, genre, length, priceComponents);
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


    @Test
    void testMapToScreeningWithoutError() throws InvalidMovieLengthException, InvalidColumnException, InvalidRowException {
        //Given
        when(movieMapper.mapToMovie(any())).thenReturn(MOVIE);
        when(roomMapper.mapToRoom(any())).thenReturn(ROOM);
        when(priceComponentMapper.mapToPriceComponents(any())).thenReturn(PRICE_COMPONENTS);

        //When
        Screening actual = screeningMapper.mapToScreening(SCREENING_ENTITY);

        //Then
        assertThat(actual, equalTo(SCREENING));
    }

    @Test
    void testMapToScreeningWithInvalidMovieLengthException() throws InvalidMovieLengthException, InvalidColumnException, InvalidRowException {
        //Given
        when(movieMapper.mapToMovie(any())).thenThrow(new InvalidMovieLengthException(""));

        //Then
        assertThrows(InvalidMovieLengthException.class, () -> {
            //When
            screeningMapper.mapToScreening(SCREENING_ENTITY);
        });
    }

    @Test
    void testMapToScreeningWithInvalidColumnException() throws InvalidMovieLengthException, InvalidColumnException, InvalidRowException {
        //Given
        when(movieMapper.mapToMovie(any())).thenReturn(MOVIE);
        when(roomMapper.mapToRoom(any())).thenThrow(new InvalidColumnException(""));

        //Then
        assertThrows(InvalidColumnException.class, () -> {
            //When
            screeningMapper.mapToScreening(SCREENING_ENTITY);
        });
    }

    @Test
    void testMapToScreeningWithInvalidRowException() throws InvalidMovieLengthException,
            InvalidColumnException, InvalidRowException {
        //Given
        when(movieMapper.mapToMovie(any())).thenReturn(MOVIE);
        when(roomMapper.mapToRoom(any())).thenThrow(new InvalidRowException(""));

        //Then
        assertThrows(InvalidRowException.class, () -> {
            //When
            screeningMapper.mapToScreening(SCREENING_ENTITY);
        });
    }

    @Test
    void testMapToScreeningEntityWithoutError() {
        //Given
        when(movieMapper.mapToMovieEntity(any())).thenReturn(MOVIE_ENTITY);
        when(roomMapper.mapToRoomEntity(any())).thenReturn(ROOM_ENTITY);
        when(priceComponentMapper.mapToPriceComponentEntities(any())).thenReturn(PRICE_COMPONENT_ENTITIES);

        //When
        ScreeningEntity actual = screeningMapper.mapToScreeningEntity(SCREENING);

        //Then
        assertThat(actual, equalTo(SCREENING_ENTITY));

    }
}