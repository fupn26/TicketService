package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.datatransfer.SeatDto;
import com.epam.training.ticketservice.domain.*;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.domain.exception.InvalidSeatException;
import com.epam.training.ticketservice.repository.BasePriceRepository;
import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.repository.SeatRepository;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.repository.exception.SeatNotFoundException;
import com.epam.training.ticketservice.service.AccountService;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private BasePriceRepository basePriceRepository;
    @Mock
    private SeatRepository seatRepository;

    private static final String USERNAME = "james";
    private static final String PASSWORD = "bond";
    private static final String MOVIE_TITLE = "best movie";
    private static final String MOVIE_GENRE = "drama";
    private static final int MOVIE_LENGTH = 100;
    private static final String ROOM_NAME = "best room";
    private static final int ROWS = 10;
    private static final int COLUMNS = 20;
    private static final LocalDateTime TIME = LocalDateTime.now();

    private static final int BASE_PRICE = 1500;
    private static final String PRICE_COMPONENT_NAME = "glamour";
    private static final int PRICE_COMPONENT_VALUE = -200;

    private static final PriceComponent PRICE_COMPONENT = new PriceComponent(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponent> PRICE_COMPONENTS = Set.of(PRICE_COMPONENT);


    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, Set.of());
    private static final Movie MOVIE_WITH_PRICE_COMPONENT = createMovie(MOVIE_GENRE, MOVIE_LENGTH, PRICE_COMPONENTS);

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(BookingServiceImplTest.MOVIE_TITLE, genre, length, priceComponents);
        } catch (InvalidMovieLengthException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final Room ROOM = createRoom(ROOM_NAME, ROWS, COLUMNS, Set.of());
    private static final Room ROOM_WITH_PRICE_COMPONENTS = createRoom(ROOM_NAME, ROWS, COLUMNS, PRICE_COMPONENTS);

    private static Room createRoom(String roomName, int rows, int columns, Set<PriceComponent> priceComponents) {
        Room result = null;
        try {
            result = new Room(roomName, rows, columns, priceComponents);
        } catch (InvalidRowException | InvalidColumnException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final UUID ID = UUID.randomUUID();
    private static final Screening SCREENING = new Screening(MOVIE, ROOM, TIME);
    private static final Screening SCREENING_WITH_PRICE_COMPONENTS_AND_ID
            = new Screening(ID, MOVIE_WITH_PRICE_COMPONENT, ROOM_WITH_PRICE_COMPONENTS, TIME, PRICE_COMPONENTS);

    private static final Seat SEAT_1 = createSeat(ROOM, ROWS - 2, COLUMNS - 1);
    private static final SeatDto SEAT_DTO_1 = new SeatDto(ROWS - 2, COLUMNS - 1);
    private static final Seat SEAT_2 = createSeat(ROOM, ROWS - 3, COLUMNS - 2);
    private static final SeatDto SEAT_DTO_2 = new SeatDto(ROWS - 3, COLUMNS - 2);
    private static final Seat SEAT_3 = createSeat(ROOM, ROWS - 1, COLUMNS - 2);
    private static final SeatDto SEAT_DTO_3 = new SeatDto(ROWS - 1, COLUMNS - 2);
    private static final SeatDto SEAT_DTO_INVALID = new SeatDto(-2, COLUMNS - 2);

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

    private static LinkedHashSet<Seat> createSeatSet(List<Seat> seatList) {
        LinkedHashSet<Seat> seatSet = new LinkedHashSet<>();
        seatSet.add(SEAT_1);
        seatSet.add(SEAT_2);
        return seatSet;
    }

    private static final Account ACCOUNT = new Account(USERNAME, PASSWORD, false);
    private static final Account PRIVILEGED_ACCOUNT = new Account(USERNAME, PASSWORD, true);
    private static final Booking BOOKING_1 = createBooking(SCREENING, ACCOUNT, SEATS, BASE_PRICE);
    private static final Booking BOOKING_WITH_PRICE_COMPONENTS = createBooking(SCREENING, ACCOUNT,
            createSeatSet(List.of(SEAT_3)), 900);

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

    @Test
    void testCreateBookingWithValidBookingReturnsValidBooking() throws ScreeningNotFoundException,
            NoSignedInAccountException, PrivilegedAccountException, SeatAlreadyBookedException,
            SeatDuplicatedException, SeatNotFoundException, InvalidColumnException, InvalidRowException {
        //Given
        when(screeningRepository.getScreeningByMovieTitleRoomNameDate(any(), any(), any())).thenReturn(SCREENING);
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);
        when(bookingRepository.getBookingsByScreening(any())).thenReturn(List.of(BOOKING_1, BOOKING_1));
        when(basePriceRepository.getBasePrice()).thenReturn(BASE_PRICE);
        when(seatRepository.getSeatByRoomNameRowColumn(any(), anyInt(), anyInt())).thenReturn(SEAT_3);

        //When
        Booking actual = bookingService.createBooking(MOVIE_TITLE, ROOM_NAME, TIME, List.of(SEAT_DTO_3));

        //Then
        verify(bookingRepository, times(1)).createBooking(any());
    }

    @Test
    void testCreateBookingWithPriceComponentsReturnsValidBooking() throws ScreeningNotFoundException,
            NoSignedInAccountException, PrivilegedAccountException, SeatAlreadyBookedException,
            SeatDuplicatedException, SeatNotFoundException, InvalidColumnException, InvalidRowException {
        //Given
        when(screeningRepository.getScreeningByMovieTitleRoomNameDate(any(), any(), any()))
                .thenReturn(SCREENING_WITH_PRICE_COMPONENTS_AND_ID);
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);
        when(bookingRepository.getBookingsByScreening(any())).thenReturn(List.of(BOOKING_1, BOOKING_1));
        when(basePriceRepository.getBasePrice()).thenReturn(BASE_PRICE);
        when(seatRepository.getSeatByRoomNameRowColumn(any(), anyInt(), anyInt())).thenReturn(SEAT_3);

        //When
        Booking actual = bookingService.createBooking(MOVIE_TITLE, ROOM_NAME, TIME, List.of(SEAT_DTO_3));

        //Then
        verify(bookingRepository, times(1)).createBooking(any());
        assertThat(actual.getPrice(), equalTo(BOOKING_WITH_PRICE_COMPONENTS.getPrice()));
    }

    @Test
    void testCreateBookingWithNonExistingScreeningThrowsScreeningNotFoundException() throws ScreeningNotFoundException,
            NoSignedInAccountException {
        //Given
        when(screeningRepository.getScreeningByMovieTitleRoomNameDate(any(), any(), any()))
                .thenThrow(ScreeningNotFoundException.class);
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);

        assertThrows(ScreeningNotFoundException.class, () -> {
            //When
            bookingService.createBooking(MOVIE_TITLE, ROOM_NAME, TIME, List.of(SEAT_DTO_3));
        });
    }

    @Test
    void testCreateBookingWithPrivilegedAccountThrowsPrivilegedAccountException() throws ScreeningNotFoundException,
            NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenReturn(PRIVILEGED_ACCOUNT);

        assertThrows(PrivilegedAccountException.class, () -> {
            //When
            bookingService.createBooking(MOVIE_TITLE, ROOM_NAME, TIME, List.of(SEAT_DTO_3));
        });
    }

    @Test
    void testCreateBookingWithNoSignedInAccountThrowsNoSignedInAccountException() throws ScreeningNotFoundException,
            NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenThrow(NoSignedInAccountException.class);

        assertThrows(NoSignedInAccountException.class, () -> {
            //When
            bookingService.createBooking(MOVIE_TITLE, ROOM_NAME, TIME, List.of(SEAT_DTO_3));
        });
    }

    @Test
    void testCreateBookingWithSeatAlreadyBookedThrowsSeatAlreadyBookedException() throws ScreeningNotFoundException,
            NoSignedInAccountException, InvalidColumnException, SeatNotFoundException, InvalidRowException {
        //Given
        when(screeningRepository.getScreeningByMovieTitleRoomNameDate(any(), any(), any()))
                .thenReturn(SCREENING);
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);
        when(bookingRepository.getBookingsByScreening(any())).thenReturn(List.of(BOOKING_1, BOOKING_1));
        when(seatRepository.getSeatByRoomNameRowColumn(any(), anyInt(), anyInt())).thenReturn(SEAT_1);

        assertThrows(SeatAlreadyBookedException.class, () -> {
            //When
            bookingService.createBooking(MOVIE_TITLE, ROOM_NAME, TIME, List.of(SEAT_DTO_1));
        });
    }

    @Test
    void testCreateBookingWithSeatDuplicatedThrowsSeatDuplicatedException() throws ScreeningNotFoundException,
            NoSignedInAccountException, InvalidColumnException, SeatNotFoundException, InvalidRowException {
        //Given
        when(screeningRepository.getScreeningByMovieTitleRoomNameDate(any(), any(), any()))
                .thenReturn(SCREENING_WITH_PRICE_COMPONENTS_AND_ID);
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);
        when(seatRepository.getSeatByRoomNameRowColumn(any(), anyInt(), anyInt()))
                .thenReturn(SEAT_1).thenReturn(SEAT_1);

        assertThrows(SeatDuplicatedException.class, () -> {
            //When
            bookingService.createBooking(MOVIE_TITLE, ROOM_NAME, TIME, List.of(SEAT_DTO_1, SEAT_DTO_1));
        });

        verify(seatRepository, times(2)).getSeatByRoomNameRowColumn(any(), anyInt(), anyInt());
    }

    @Test
    void testCreateBookingWithInvalidSeatThrowsSeatNotFoundException() throws ScreeningNotFoundException,
            NoSignedInAccountException, InvalidColumnException, SeatNotFoundException, InvalidRowException {
        //Given
        when(screeningRepository.getScreeningByMovieTitleRoomNameDate(any(), any(), any()))
                .thenReturn(SCREENING_WITH_PRICE_COMPONENTS_AND_ID);
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);
        when(seatRepository.getSeatByRoomNameRowColumn(any(), anyInt(), anyInt()))
                .thenThrow(InvalidRowException.class);

        assertThrows(SeatNotFoundException.class, () -> {
            //When
            bookingService.createBooking(MOVIE_TITLE, ROOM_NAME, TIME, List.of(SEAT_DTO_INVALID, SEAT_DTO_1));
        });
    }
}