package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.BookingDao;
import com.epam.training.ticketservice.dataccess.entity.AccountEntity;
import com.epam.training.ticketservice.dataccess.entity.BookingEntity;
import com.epam.training.ticketservice.dataccess.entity.MovieEntity;
import com.epam.training.ticketservice.dataccess.entity.RoomEntity;
import com.epam.training.ticketservice.dataccess.entity.ScreeningEntity;
import com.epam.training.ticketservice.dataccess.entity.SeatEntity;
import com.epam.training.ticketservice.domain.*;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.domain.exception.InvalidSeatException;
import com.epam.training.ticketservice.repository.mapper.AccountMapper;
import com.epam.training.ticketservice.repository.mapper.ScreeningMapper;
import com.epam.training.ticketservice.repository.mapper.SeatMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingRepositoryImplTest {

    @InjectMocks
    private BookingRepositoryImpl bookingRepository;
    @Mock
    private BookingDao bookingDao;
    @Mock
    private ScreeningMapper screeningMapper;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private SeatMapper seatMapper;

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


    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, Set.of());
    private static final MovieEntity MOVIE_ENTITY = new MovieEntity(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH, Set.of());
    private static final MovieEntity INVALID_LENGTH_MOVIE_ENTITY
            = new MovieEntity(MOVIE_TITLE, MOVIE_GENRE, -MOVIE_LENGTH, Set.of());

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(BookingRepositoryImplTest.MOVIE_TITLE, genre, length, priceComponents);
        } catch (InvalidMovieLengthException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final Room ROOM = createRoom(ROOM_NAME, ROWS, COLUMNS, Set.of());
    private static final RoomEntity ROOM_ENTITY = new RoomEntity(ROOM_NAME, ROWS, COLUMNS, Set.of());
    private static final RoomEntity INVALID_ROWS_ROOM_ENTITY = new RoomEntity(ROOM_NAME, -ROWS, COLUMNS, Set.of());
    private static final RoomEntity INVALID_COLUMNS_ROOM_ENTITY = new RoomEntity(ROOM_NAME, ROWS, -COLUMNS, Set.of());

    private static Room createRoom(String roomName, int rows, int columns, Set<PriceComponent> priceComponents) {
        Room result = null;
        try {
            result = new Room(roomName, rows, columns, priceComponents);
        } catch (InvalidRowException | InvalidColumnException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final Screening SCREENING
            = new Screening(MOVIE, ROOM, TIME);
    private static final ScreeningEntity SCREENING_ENTITY
            = new ScreeningEntity(MOVIE_ENTITY, ROOM_ENTITY, TIME, Set.of());
    private static final ScreeningEntity INVALID_ROWS_SCREENING_ENTITY
            = new ScreeningEntity(MOVIE_ENTITY, INVALID_ROWS_ROOM_ENTITY, TIME, Set.of());
    private static final ScreeningEntity INVALID_COLUMNS_SCREENING_ENTITY
            = new ScreeningEntity(MOVIE_ENTITY, INVALID_COLUMNS_ROOM_ENTITY, TIME, Set.of());
    private static final ScreeningEntity INVALID_LENGTH_SCREENING_ENTITY
            = new ScreeningEntity(INVALID_LENGTH_MOVIE_ENTITY, ROOM_ENTITY, TIME, Set.of());

    private static final Seat SEAT_1 = createSeat(ROOM, ROWS - 2, COLUMNS - 1);
    private static final Seat SEAT_2 = createSeat(ROOM, ROWS - 3, COLUMNS - 2);
    private static final SeatEntity SEAT_ENTITY_1
            = new SeatEntity(ROOM_ENTITY, ROWS - 2, COLUMNS - 1);
    private static final SeatEntity SEAT_ENTITY_2
            = new SeatEntity(ROOM_ENTITY, ROWS - 3, COLUMNS - 2);
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
    private static final List<SeatEntity> SEAT_ENTITIES = List.of(SEAT_ENTITY_1, SEAT_ENTITY_2);

    private static LinkedHashSet<Seat> createSeatSet(List<Seat> seatList) {
        LinkedHashSet<Seat> seatSet = new LinkedHashSet<>();
        seatSet.add(SEAT_1);
        seatSet.add(SEAT_2);
        return seatSet;
    }

    private static final Account ACCOUNT = new Account(USERNAME, PASSWORD, false);
    private static final AccountEntity ACCOUNT_ENTITY = new AccountEntity(USERNAME, PASSWORD, false);
    private static final Booking BOOKING = createBooking(SCREENING, ACCOUNT, SEATS, BASE_PRICE);
    private static final List<Booking> BOOKINGS = List.of(BOOKING);
    private static final BookingEntity BOOKING_ENTITY
            = new BookingEntity(SCREENING_ENTITY, ACCOUNT_ENTITY, SEAT_ENTITIES, BASE_PRICE);
    private static final BookingEntity INVALID_MOVIE_LENGTH_BOOKING_ENTITY
            = new BookingEntity(INVALID_LENGTH_SCREENING_ENTITY, ACCOUNT_ENTITY, SEAT_ENTITIES, BASE_PRICE);
    private static final BookingEntity INVALID_ROWS_BOOKING_ENTITY
            = new BookingEntity(INVALID_ROWS_SCREENING_ENTITY, ACCOUNT_ENTITY, SEAT_ENTITIES, BASE_PRICE);
    private static final BookingEntity INVALID_COLUMNS_BOOKING_ENTITY
            = new BookingEntity(INVALID_COLUMNS_SCREENING_ENTITY, ACCOUNT_ENTITY, SEAT_ENTITIES, BASE_PRICE);

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
    void testCreateBookingWithSuccess() {
        //Given
        when(accountMapper.mapToAccountEntity(any())).thenReturn(ACCOUNT_ENTITY);
        when(screeningMapper.mapToScreeningEntity(any())).thenReturn(SCREENING_ENTITY);
        when(seatMapper.mapToSeatEntity(any())).thenReturn(SEAT_ENTITY_1).thenReturn(SEAT_ENTITY_2);

        //When
        bookingRepository.createBooking(BOOKING);

        //Then
        verify(accountMapper, times(1)).mapToAccountEntity(ACCOUNT);
        verify(screeningMapper, times(1)).mapToScreeningEntity(SCREENING);
        verify(seatMapper, times(2)).mapToSeatEntity(any());
        ArgumentCaptor<BookingEntity> argumentCaptor = ArgumentCaptor.forClass(BookingEntity.class);
        verify(bookingDao, times(1)).save(argumentCaptor.capture());
        BookingEntity actual = argumentCaptor.getValue();
        assertThat(actual.getScreening(), equalTo(SCREENING_ENTITY));
        assertThat(actual.getAccount(), equalTo(ACCOUNT_ENTITY));
        assertThat(actual.getSeats(), equalTo(SEAT_ENTITIES));
    }

    @Test
    void testGetBookingsByUserNameWithoutMappingProblems()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByAccount_Username(any())).thenReturn(List.of(BOOKING_ENTITY));
        when(accountMapper.mapToAccount(any())).thenReturn(ACCOUNT);
        when(screeningMapper.mapToScreening(any())).thenReturn(SCREENING);
        when(seatMapper.mapToSeat(any())).thenReturn(SEAT_1).thenReturn(SEAT_2);

        //When
        List<Booking> actual = bookingRepository.getBookingsByUserName(USERNAME);

        //Then
        verify(bookingDao, times(1)).findAllByAccount_Username(USERNAME);
        assertThat(actual, equalTo(BOOKINGS));
    }

    @Test
    void testGetBookingsByUserNameWithInvalidMovieLengthReturnsEmptyList()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByAccount_Username(any())).thenReturn(List.of(INVALID_MOVIE_LENGTH_BOOKING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenThrow(InvalidMovieLengthException.class);

        //When
        List<Booking> actual = bookingRepository.getBookingsByUserName(USERNAME);

        //Then
        verify(bookingDao, times(1)).findAllByAccount_Username(USERNAME);
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void testGetBookingsByUserNameWithInvalidRowsReturnsEmptyList()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByAccount_Username(any())).thenReturn(List.of(INVALID_ROWS_BOOKING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenThrow(InvalidRowException.class);

        //When
        List<Booking> actual = bookingRepository.getBookingsByUserName(USERNAME);

        //Then
        verify(bookingDao, times(1)).findAllByAccount_Username(USERNAME);
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void testGetBookingsByUserNameWithInvalidColumnsReturnsEmptyList()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByAccount_Username(any())).thenReturn(List.of(INVALID_COLUMNS_BOOKING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenThrow(InvalidColumnException.class);

        //When
        List<Booking> actual = bookingRepository.getBookingsByUserName(USERNAME);

        //Then
        verify(bookingDao, times(1)).findAllByAccount_Username(USERNAME);
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void testGetBookingsByUserNameWithInvalidRowSeatReturnsEmptySeatList()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByAccount_Username(any())).thenReturn(List.of(BOOKING_ENTITY));
        when(accountMapper.mapToAccount(any())).thenReturn(ACCOUNT);
        when(screeningMapper.mapToScreening(any())).thenReturn(SCREENING);
        when(seatMapper.mapToSeat(any())).thenThrow(InvalidRowException.class);

        //When
        List<Booking> actual = bookingRepository.getBookingsByUserName(USERNAME);

        //Then
        verify(bookingDao, times(1)).findAllByAccount_Username(USERNAME);
        assertThat(actual.get(0).getSeatList(), equalTo(Set.of()));
    }

    @Test
    void testGetBookingsByUserNameWithInvalidColumnSeatReturnsEmptySeatList()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByAccount_Username(any())).thenReturn(List.of(BOOKING_ENTITY));
        when(accountMapper.mapToAccount(any())).thenReturn(ACCOUNT);
        when(screeningMapper.mapToScreening(any())).thenReturn(SCREENING);
        when(seatMapper.mapToSeat(any())).thenThrow(InvalidColumnException.class);

        //When
        List<Booking> actual = bookingRepository.getBookingsByUserName(USERNAME);

        //Then
        verify(bookingDao, times(1)).findAllByAccount_Username(USERNAME);
        assertThat(actual.get(0).getSeatList(), equalTo(Set.of()));
    }

    @Test
    void getBookingsByScreeningReturnsBookings()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByScreening(any())).thenReturn(List.of(BOOKING_ENTITY));
        when(accountMapper.mapToAccount(any())).thenReturn(ACCOUNT);
        when(screeningMapper.mapToScreening(any())).thenReturn(SCREENING);
        when(screeningMapper.mapToScreeningEntity(any())).thenReturn(SCREENING_ENTITY);
        when(seatMapper.mapToSeat(any())).thenReturn(SEAT_1).thenReturn(SEAT_2);

        //When
        List<Booking> actual = bookingRepository.getBookingsByScreening(SCREENING);

        //Then
        verify(bookingDao, times(1)).findAllByScreening(SCREENING_ENTITY);
        assertThat(actual, equalTo(BOOKINGS));
    }

    @Test
    void getBookingByScreeningWithInvalidMovieLengthReturnsEmptyList()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByScreening(any())).thenReturn(List.of(BOOKING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenThrow(InvalidMovieLengthException.class);
        when(screeningMapper.mapToScreeningEntity(any())).thenReturn(SCREENING_ENTITY);

        //When
        List<Booking> actual = bookingRepository.getBookingsByScreening(SCREENING);

        //Then
        verify(bookingDao, times(1)).findAllByScreening(any());
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void getBookingByScreeningWithInvalidColumnsRoomReturnsEmptyList()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByScreening(any())).thenReturn(List.of(BOOKING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenThrow(InvalidColumnException.class);
        when(screeningMapper.mapToScreeningEntity(any())).thenReturn(SCREENING_ENTITY);

        //When
        List<Booking> actual = bookingRepository.getBookingsByScreening(SCREENING);

        //Then
        verify(bookingDao, times(1)).findAllByScreening(any());
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void getBookingByScreeningWithInvalidRowsRoomReturnsEmptyList()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByScreening(any())).thenReturn(List.of(BOOKING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenThrow(InvalidRowException.class);
        when(screeningMapper.mapToScreeningEntity(any())).thenReturn(SCREENING_ENTITY);

        //When
        List<Booking> actual = bookingRepository.getBookingsByScreening(SCREENING);

        //Then
        verify(bookingDao, times(1)).findAllByScreening(any());
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void getBookingByScreeningWithInvalidRowSeatReturnsEmptySeatList()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByScreening(any())).thenReturn(List.of(BOOKING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenReturn(SCREENING);
        when(screeningMapper.mapToScreeningEntity(any())).thenReturn(SCREENING_ENTITY);
        when(accountMapper.mapToAccount(any())).thenReturn(ACCOUNT);
        when(seatMapper.mapToSeat(any())).thenThrow(InvalidRowException.class);

        //When
        List<Booking> actual = bookingRepository.getBookingsByScreening(SCREENING);

        //Then
        verify(bookingDao, times(1)).findAllByScreening(any());
        assertThat(actual.get(0).getSeatList(), equalTo(Set.of()));
    }

    @Test
    void getBookingByScreeningWithInvalidColumnSeatReturnsEmptySeatList()
            throws InvalidColumnException, InvalidMovieLengthException, InvalidRowException {
        //Given
        when(bookingDao.findAllByScreening(any())).thenReturn(List.of(BOOKING_ENTITY));
        when(screeningMapper.mapToScreening(any())).thenReturn(SCREENING);
        when(screeningMapper.mapToScreeningEntity(any())).thenReturn(SCREENING_ENTITY);
        when(accountMapper.mapToAccount(any())).thenReturn(ACCOUNT);
        when(seatMapper.mapToSeat(any())).thenThrow(InvalidColumnException.class);

        //When
        List<Booking> actual = bookingRepository.getBookingsByScreening(SCREENING);

        //Then
        verify(bookingDao, times(1)).findAllByScreening(any());
        assertThat(actual.get(0).getSeatList(), equalTo(Set.of()));
    }

    @Test
    void testDeleteAllByScreening() {
        //Given
        when(screeningMapper.mapToScreeningEntity(any())).thenReturn(SCREENING_ENTITY);
        //When
        bookingRepository.deleteAllByScreening(SCREENING);

        //Then
        verify(bookingDao, times(1)).deleteAllByScreening(SCREENING_ENTITY);
    }

    @Test
    void testDeleteAllByMovieTitle() {
        //When
        bookingRepository.deleteAllByMovieTitle(MOVIE_TITLE);

        //Then
        verify(bookingDao, times(1)).deleteAllByScreening_Movie_Title(MOVIE_TITLE);
    }

    @Test
    void testDeleteAllByRoomName() {
        //When
        bookingRepository.deleteAllByRoomName(ROOM_NAME);

        //Then
        verify(bookingDao, times(1)).deleteAllByScreening_Room_Name(ROOM_NAME);
    }

    @Test
    void testDeleteAllBySeat() {
        //Given
        when(seatMapper.mapToSeatEntity(any())).thenReturn(SEAT_ENTITY_1);

        //When
        bookingRepository.deleteAllBySeat(SEAT_1);

        //Then
        verify(bookingDao, times(1)).deleteAllBySeatsIsContaining(SEAT_ENTITY_1);
    }
}