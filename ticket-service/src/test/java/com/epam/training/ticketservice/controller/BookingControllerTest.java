package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.controller.mapper.DateTimeMapper;
import com.epam.training.ticketservice.datatransfer.SeatDto;
import com.epam.training.ticketservice.domain.*;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.domain.exception.InvalidSeatException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.repository.exception.SeatNotFoundException;
import com.epam.training.ticketservice.service.BookingService;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import com.epam.training.ticketservice.service.exception.PrivilegedAccountException;
import com.epam.training.ticketservice.service.exception.SeatAlreadyBookedException;
import com.epam.training.ticketservice.service.exception.SeatDuplicatedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;
    @Mock
    private BookingService bookingService;
    @Mock
    private DateTimeMapper dateTimeMapper;

    private static final String USERNAME = "james";
    private static final String PASSWORD = "bond";
    private static final String MOVIE_TITLE = "best movie";
    private static final String MOVIE_GENRE = "drama";
    private static final int MOVIE_LENGTH = 100;
    private static final String ROOM_NAME = "best room";
    private static final int ROWS = 10;
    private static final int COLUMNS = 20;
    private static final LocalDateTime TIME = LocalDateTime.now();
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final String TIME_STRING = TIME.format(DATE_TIME_FORMATTER);

    private static final int BASE_PRICE = 1500;

    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, Set.of());

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(BookingControllerTest.MOVIE_TITLE, genre, length, priceComponents);
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

    private static final Screening SCREENING = new Screening(MOVIE, ROOM, TIME);

    private static final Seat SEAT_1 = createSeat(ROOM, ROWS - 2, COLUMNS - 1);
    private static final SeatDto SEAT_DTO_1 = new SeatDto(ROWS - 2, COLUMNS - 1);
    private static final Seat SEAT_2 = createSeat(ROOM, ROWS - 3, COLUMNS - 2);
    private static final SeatDto SEAT_DTO_2 = new SeatDto(ROWS - 3, COLUMNS - 2);

    private static Seat createSeat(Room room, int rowNum, int columnNum) {
        Seat result = null;
        try {
            result = new Seat(ROOM, rowNum, columnNum);
        } catch (InvalidRowException | InvalidColumnException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final LinkedHashSet<Seat> SEATS = createSeatSet(List.of(SEAT_1, SEAT_2));
    private static final List<SeatDto> SEAT_DTOS = List.of(SEAT_DTO_1, SEAT_DTO_2);
    private static final String SEAT_STRING_LIST_INPUT = String.format("%d,%d %d,%d",
            SEAT_1.getRowNum(), SEAT_1.getColumnNum(), SEAT_2.getRowNum(), SEAT_2.getColumnNum());
    private static final String SEAT_STRING_LIST_OUTPUT = String.format("(%d,%d), (%d,%d)",
            SEAT_1.getRowNum(), SEAT_1.getColumnNum(), SEAT_2.getRowNum(), SEAT_2.getColumnNum());
    private static final String BOOK_SUCCESS_MESSAGE
            = String.format("Seats booked: %s; the price for this booking is %d HUF", SEAT_STRING_LIST_OUTPUT,
            BASE_PRICE);
    private static final String SEAT_LIST_PARSE_ERROR
            = "Seat list can't be parsed. Use this format: '<row>,<column> <row>,<column>'";


    private static LinkedHashSet<Seat> createSeatSet(List<Seat> seatList) {
        LinkedHashSet<Seat> seatSet = new LinkedHashSet<>();
        seatSet.add(SEAT_1);
        seatSet.add(SEAT_2);
        return seatSet;
    }

    private static final Account ACCOUNT = new Account(USERNAME, PASSWORD, false);
    private static final Booking BOOKING = createBooking(SCREENING, ACCOUNT, SEATS, BASE_PRICE);

    private static  Booking createBooking(Screening screening, Account account,
                                          LinkedHashSet<Seat> seats, int basePrice) {
        Booking result = null;
        try {
            result = new Booking(screening, account, seats, basePrice);
        } catch (InvalidSeatException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final SeatDuplicatedException SEAT_DUPLICATED_EXCEPTION
            = new SeatDuplicatedException("Seat duplicated");
    private static final ScreeningNotFoundException SCREENING_NOT_FOUND_EXCEPTION
            = new ScreeningNotFoundException("Screening not found");
    private static final SeatNotFoundException SEAT_NOT_FOUND_EXCEPTION
            = new SeatNotFoundException("Screen not found", ROOM_NAME, SEAT_1.getRowNum(), SEAT_1.getColumnNum());
    private static final String SEAT_NOT_FOUND_MESSAGE = String.format("Seat (%d,%d) does not exist in this room",
            SEAT_1.getRowNum(), SEAT_1.getColumnNum());
    private static final SeatAlreadyBookedException SEAT_ALREADY_BOOKED_EXCEPTION
            = new SeatAlreadyBookedException("Seat already booked", SEAT_1);
    private static final String SEAT_ALREADY_TAKEN_MESSAGE = String.format("Seat (%d,%d) is already taken",
            SEAT_1.getRowNum(), SEAT_1.getColumnNum());

    @Test
    void testBookWithValidInputReturnsSuccessString() throws SeatAlreadyBookedException,
            SeatDuplicatedException, SeatNotFoundException, ScreeningNotFoundException,
            PrivilegedAccountException, NoSignedInAccountException {
        //Given
        when(bookingService.createBooking(any(), any(), any(), any())).thenReturn(BOOKING);
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(TIME);

        //When
        String actual = bookingController.book(MOVIE_TITLE, ROOM_NAME, TIME_STRING, SEAT_STRING_LIST_INPUT);

        //Then
        verify(bookingService, times(1)).createBooking(MOVIE_TITLE, ROOM_NAME, TIME, SEAT_DTOS);
        assertThat(actual, equalTo(BOOK_SUCCESS_MESSAGE));
    }

    @Test
    void testBookWithEmptySeatListReturnsSeatParseErrorString() throws SeatAlreadyBookedException, RoomNotFoundException,
            MovieNotFoundException, SeatDuplicatedException, SeatNotFoundException {
        //Given
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(TIME);


        //When
        String actual = bookingController.book(MOVIE_TITLE, ROOM_NAME, TIME_STRING, "");

        //Then
        assertThat(actual, equalTo(SEAT_LIST_PARSE_ERROR));
    }

    @Test
    void testBookWithSemicolonSeatListReturnsSeatParseErrorString() {
        //Given
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(TIME);


        //When
        String actual = bookingController.book(MOVIE_TITLE, ROOM_NAME, TIME_STRING, "5;4 6;7");

        //Then
        assertThat(actual, equalTo(SEAT_LIST_PARSE_ERROR));
    }

    @Test
    void testBookWithMultipleSpaceSeatListReturnsSeatParseErrorString() {
        //Given
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(TIME);


        //When
        String actual = bookingController.book(MOVIE_TITLE, ROOM_NAME, TIME_STRING, "5,6  4,5");

        //Then
        assertThat(actual, equalTo(SEAT_LIST_PARSE_ERROR));
    }

    @Test
    void testBookWithNonExistingScreeningReturnsScreeningNotFoundExceptionMessage() throws SeatAlreadyBookedException,
            SeatDuplicatedException, SeatNotFoundException, ScreeningNotFoundException,
            PrivilegedAccountException, NoSignedInAccountException {
        //Given
        when(bookingService.createBooking(any(), any(), any(), any())).thenThrow(SCREENING_NOT_FOUND_EXCEPTION);
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(TIME);

        //When
        String actual = bookingController.book(MOVIE_TITLE, ROOM_NAME, TIME_STRING, SEAT_STRING_LIST_INPUT);

        //Then
        assertThat(actual, equalTo(SCREENING_NOT_FOUND_EXCEPTION.getMessage()));
    }

    @Test
    void testBookWithDuplicatedSeatReturnsSeatDuplicatedExceptionMessage() throws SeatAlreadyBookedException,
            SeatDuplicatedException, SeatNotFoundException, ScreeningNotFoundException,
            PrivilegedAccountException, NoSignedInAccountException {
        //Given
        when(bookingService.createBooking(any(), any(), any(), any())).thenThrow(SEAT_DUPLICATED_EXCEPTION);
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(TIME);

        //When
        String actual = bookingController.book(MOVIE_TITLE, ROOM_NAME, TIME_STRING, SEAT_STRING_LIST_INPUT);

        //Then
        assertThat(actual, equalTo(SEAT_DUPLICATED_EXCEPTION.getMessage()));
    }

    @Test
    void testBookWithNotExistingSeatReturnsSeatNotFoundMessage() throws SeatAlreadyBookedException,
            SeatDuplicatedException, SeatNotFoundException, ScreeningNotFoundException,
            PrivilegedAccountException, NoSignedInAccountException {
        //Given
        when(bookingService.createBooking(any(), any(), any(), any())).thenThrow(SEAT_NOT_FOUND_EXCEPTION);
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(TIME);

        //When
        String actual = bookingController.book(MOVIE_TITLE, ROOM_NAME, TIME_STRING, SEAT_STRING_LIST_INPUT);

        //Then
        assertThat(actual, equalTo(SEAT_NOT_FOUND_MESSAGE));
    }

    @Test
    void testBookWithBookedSeatReturnsSeatAlreadyTakenMessage() throws SeatAlreadyBookedException,
            SeatDuplicatedException, SeatNotFoundException, ScreeningNotFoundException,
            PrivilegedAccountException, NoSignedInAccountException {
        //Given
        when(bookingService.createBooking(any(), any(), any(), any())).thenThrow(SEAT_ALREADY_BOOKED_EXCEPTION);
        when(dateTimeMapper.mapToLocalDateTime(any(), any())).thenReturn(TIME);

        //When
        String actual = bookingController.book(MOVIE_TITLE, ROOM_NAME, TIME_STRING, SEAT_STRING_LIST_INPUT);

        //Then
        assertThat(actual, equalTo(SEAT_ALREADY_TAKEN_MESSAGE));
    }
}