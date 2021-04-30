package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.controller.mapper.DateTimeMapper;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.exception.*;
import com.epam.training.ticketservice.service.ScreeningService;
import com.epam.training.ticketservice.service.exception.BreakTimeException;
import com.epam.training.ticketservice.service.exception.OverlappingScreeningException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreeningControllerTest {

    @InjectMocks
    private ScreeningController screeningController;
    @Mock
    private ScreeningService screeningService;
    @Mock
    private DateTimeMapper dateTimeMapper;

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String MOVIE_TITLE = "james bond";
    private static final String ROOM_NAME = "best room";
    private static final String SCREENING_DATE_TIME = "2020-01-12 11:06";
    private static final String SCREENING_CREATION_SUCCESS = String.format("Screening created: %s, %s, %s",
            MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);
    private static final String SCREENING_DELETION_SUCCESS = String.format("Screening deleted: %s, %s, %s",
            MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);
    private static final String SCREENING_OVERLAPPING_MESSAGE = "There is an overlapping screening";
    private static final String NO_SCREENINGS_MESSAGE = "There are no screenings";
    private static final String SCREENING_IN_BREAK_TIME
            = "This would start in the break period after another screening in this room";
    private static final String SCREENING_TIME_INVALID_MESSAGE
            = "Invalid screen date time. Use this format: yyyy-MM-dd HH:mm";
    private static final String SCREENING_STRING_TEMPLATE
            = "%s (%s, %s minutes), screened in room %s, at %s";
    private static final RoomNotFoundException ROOM_NOT_FOUND_EXCEPTION = new RoomNotFoundException("Room not found");
    private static final ScreeningAlreadyExistsException SCREENING_ALREADY_EXISTS_EXCEPTION
            = new ScreeningAlreadyExistsException("Screening exists");
    private static final ScreeningNotFoundException SCREENING_NOT_FOUND_EXCEPTION
            = new ScreeningNotFoundException("Screening not found");
    private static final ScreeningMalformedException SCREENING_MALFORMED_EXCEPTION
            = new ScreeningMalformedException("Screening malformed");
    private static final MovieNotFoundException MOVIE_NOT_FOUND_EXCEPTION
            = new MovieNotFoundException("Movie not found");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private static final LocalDateTime DATE_TIME = LocalDateTime.parse(SCREENING_DATE_TIME, DATE_TIME_FORMATTER);

    private static final String MOVIE_GENRE = "drama";
    private static final int MOVIE_LENGTH = 100;
    private static final int ROWS = 10;
    private static final int COLUMNS = 20;


    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, Set.of());

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(ScreeningControllerTest.MOVIE_TITLE, genre, length, priceComponents);
        } catch (InvalidMovieLengthException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final Room ROOM = createRoom(ROOM_NAME, ROWS, COLUMNS, Set.of());

    private static Room createRoom(String roomName, int rows, int columns, Set<PriceComponent> priceComponents) {
        Room result = null;
        try {
            result = new Room(roomName, rows, columns, priceComponents);
        } catch (InvalidRowException | InvalidColumnException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final Screening SCREENING = new Screening(MOVIE, ROOM, DATE_TIME);
    private static final List<Screening> SCREENINGS = List.of(SCREENING, SCREENING, SCREENING);
    private static final String SCREENINGS_STRING = SCREENINGS.stream()
            .map(screening ->
                String.format(SCREENING_STRING_TEMPLATE,
                        screening.getMovie().getTitle(),
                        screening.getMovie().getGenre(),
                        screening.getMovie().getLength(),
                        screening.getRoom().getName(),
                        screening.getStartDate().format(DATE_TIME_FORMATTER))
            )
            .collect(Collectors.joining(System.lineSeparator()));


    @Test
    void testCreateScreeningWithNonExistingValidScreeningReturnsSuccessString() throws RoomNotFoundException,
            ScreeningAlreadyExistsException, OverlappingScreeningException,
            BreakTimeException, MovieNotFoundException {
        //Given
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(DATE_TIME);

        //When
        String actual = screeningController.createScreening(MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);

        //Then
        verify(screeningService, times(1)).createScreening(any(), any(), any());
        assertThat(actual, equalTo(SCREENING_CREATION_SUCCESS));
    }

    @Test
    void testCreateScreeningWithExistingValidScreeningReturnsScreeningAlreadyExistsExceptionMessage()
            throws RoomNotFoundException, ScreeningAlreadyExistsException, OverlappingScreeningException,
            BreakTimeException, MovieNotFoundException {
        //Given
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(DATE_TIME);

        //Given
        doThrow(SCREENING_ALREADY_EXISTS_EXCEPTION)
                .when(screeningService)
                .createScreening(any(), any(), any());

        //When
        String actual = screeningController.createScreening(MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);

        //Then
        verify(screeningService, times(1)).createScreening(any(), any(), any());
        assertThat(actual, equalTo(SCREENING_ALREADY_EXISTS_EXCEPTION.getMessage()));
    }

    @Test
    void testCreateScreeningWithInvalidScreeningDateTimeReturnsInvalidScreeningTimeMessage()
            throws RoomNotFoundException, ScreeningAlreadyExistsException, OverlappingScreeningException,
            BreakTimeException, MovieNotFoundException {
        //Given
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenThrow(DateTimeParseException.class);

        //When
        String actual = screeningController.createScreening(MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);

        //Then
        assertThat(actual, equalTo(SCREENING_TIME_INVALID_MESSAGE));
    }

    @Test
    void testCreateScreeningWithValidScreeningReturnsMovieNotFoundExceptionMessage()
            throws RoomNotFoundException, ScreeningAlreadyExistsException, OverlappingScreeningException,
            BreakTimeException, MovieNotFoundException {
        //Given
        doThrow(MOVIE_NOT_FOUND_EXCEPTION)
                .when(screeningService)
                .createScreening(any(), any(), any());
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(DATE_TIME);

        //When
        String actual = screeningController.createScreening(MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);

        //Then
        verify(screeningService, times(1)).createScreening(any(), any(), any());
        assertThat(actual, equalTo(MOVIE_NOT_FOUND_EXCEPTION.getMessage()));
    }

    @Test
    void testCreateScreeningWithValidScreeningReturnsRoomNotFoundExceptionMessage()
            throws RoomNotFoundException, ScreeningAlreadyExistsException, OverlappingScreeningException,
            BreakTimeException, MovieNotFoundException {
        //Given
        doThrow(ROOM_NOT_FOUND_EXCEPTION)
                .when(screeningService)
                .createScreening(any(), any(), any());
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(DATE_TIME);

        //When
        String actual = screeningController.createScreening(MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);

        //Then
        verify(screeningService, times(1)).createScreening(any(), any(), any());
        assertThat(actual, equalTo(ROOM_NOT_FOUND_EXCEPTION.getMessage()));
    }

    @Test
    void testCreateScreeningWithValidScreeningReturnsOverlappingScreeningMessage()
            throws RoomNotFoundException, ScreeningAlreadyExistsException, OverlappingScreeningException,
            BreakTimeException, MovieNotFoundException {
        //Given
        doThrow(OverlappingScreeningException.class)
                .when(screeningService)
                .createScreening(any(), any(), any());
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(DATE_TIME);

        //When
        String actual = screeningController.createScreening(MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);

        //Then
        verify(screeningService, times(1)).createScreening(any(), any(), any());
        assertThat(actual, equalTo(SCREENING_OVERLAPPING_MESSAGE));
    }

    @Test
    void testCreateScreeningWithValidScreeningReturnsScreeningInBreakMessage()
            throws RoomNotFoundException, ScreeningAlreadyExistsException, OverlappingScreeningException,
            BreakTimeException, MovieNotFoundException {
        //Given
        doThrow(BreakTimeException.class)
                .when(screeningService)
                .createScreening(any(), any(), any());
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(DATE_TIME);

        //When
        String actual = screeningController.createScreening(MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);

        //Then
        verify(screeningService, times(1)).createScreening(any(), any(), any());
        assertThat(actual, equalTo(SCREENING_IN_BREAK_TIME));
    }

    @Test
    void testListScreeningsWithExistingScreeningsReturnsScreenings() {
        //Given
        when(screeningService.getAllScreenings()).thenReturn(SCREENINGS);
        when(dateTimeMapper.mapToString(any(), any())).thenReturn(SCREENING_DATE_TIME);

        //When
        String actual = screeningController.listScreenings();

        //Then
        verify(screeningService, times(1)).getAllScreenings();
        verify(dateTimeMapper, times(SCREENINGS.size())).mapToString(DATE_TIME, DATE_TIME_FORMAT);
        assertThat(actual, equalTo(SCREENINGS_STRING));
    }

    @Test
    void testListScreeningsWithoutExistingScreeningsReturnsEmptyList() {
        //Given
        when(screeningService.getAllScreenings()).thenReturn(List.of());

        //When
        String actual = screeningController.listScreenings();

        //Then
        verify(screeningService, times(1)).getAllScreenings();
        assertThat(actual, equalTo(NO_SCREENINGS_MESSAGE));
    }

    @Test
    void testDeleteScreeningWithExistingValidScreeningReturnsSuccessMessage() throws ScreeningNotFoundException {
        //Given
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(DATE_TIME);

        //When
        String actual = screeningController.deleteScreening(MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);

        //Then
        verify(screeningService, times(1)).deleteScreening(MOVIE_TITLE, ROOM_NAME, DATE_TIME);
        assertThat(actual, equalTo(SCREENING_DELETION_SUCCESS));
    }

    @Test
    void testDeleteScreeningWithNonExistingValidScreeningReturnsScreeningNotFoundExceptionMessage()
            throws ScreeningNotFoundException {
        //Given
        doThrow(SCREENING_NOT_FOUND_EXCEPTION)
                .when(screeningService)
                .deleteScreening(any(), any(), any());
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(DATE_TIME);

        //When
        String actual = screeningController.deleteScreening(MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);

        //Then
        verify(screeningService, times(1)).deleteScreening(MOVIE_TITLE, ROOM_NAME, DATE_TIME);
        assertThat(actual, equalTo(SCREENING_NOT_FOUND_EXCEPTION.getMessage()));
    }

    @Test
    void testDeleteScreeningWithInvalidScreeningReturnsInvalidScreeningTimeMessage()
            throws ScreeningNotFoundException {
        //Given
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenThrow(DateTimeParseException.class);

        //When
        String actual = screeningController.deleteScreening(MOVIE_TITLE, ROOM_NAME, SCREENING_DATE_TIME);

        //Then
        assertThat(actual, equalTo(SCREENING_TIME_INVALID_MESSAGE));
    }
}