package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.domain.*;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.domain.exception.InvalidSeatException;
import com.epam.training.ticketservice.repository.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import com.epam.training.ticketservice.service.exception.PrivilegedAccountException;
import com.epam.training.ticketservice.service.exception.SignInFailedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;
    @Mock
    private AccountService accountService;

    private final static String USERNAME = "james";
    private final static String PASSWORD = "bond";
    private final static String SUCCESS = null;
    private final static String SIGN_IN_FAIL = "Login failed due to incorrect credentials";
    private final static String SIGN_UP_FAIL = String.format("Account with username '%s' already exists",
            USERNAME);
    private static final String NO_SIGNED_IN_ACCOUNT = "You are not signed in";
    private static final String MOVIE_TITLE = "best movie";
    private static final String MOVIE_GENRE = "drama";
    private static final int MOVIE_LENGTH = 100;
    private static final String ROOM_NAME = "best room";
    private static final int ROWS = 10;
    private static final int COLUMNS = 20;
    private static final LocalDateTime TIME = LocalDateTime.now();
    private static final int PRICE = 1500;

    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, Set.of());

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(AccountControllerTest.MOVIE_TITLE, genre, length, priceComponents);
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

    private static final Screening SCREENING = new Screening(MOVIE, ROOM, TIME, Set.of());

    private static final Seat SEAT_1 = createSeat(ROOM, ROWS - 2, COLUMNS - 1);
    private static final Seat SEAT_2 = createSeat(ROOM, ROWS - 3, COLUMNS - 2);

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

    private static LinkedHashSet<Seat> createSeatSet(List<Seat> seatList) {
        LinkedHashSet<Seat> seatSet = new LinkedHashSet<>();
        seatSet.add(SEAT_1);
        seatSet.add(SEAT_2);
        return seatSet;
    }

    private static final Account ACCOUNT = new Account(USERNAME, PASSWORD, false);
    private static final Account PRIVILEGED_ACCOUNT = new Account(USERNAME, PASSWORD, true);
    private static final Booking BOOKING = createBooking(SCREENING, ACCOUNT, SEATS, PRICE);

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

    private static final String NON_PRIVILEGED_LOGIN_WITH_BOOKINGS = "Signed in with account '"
            + USERNAME
            + "'"
            + System.lineSeparator()
            + "Your previous bookings are"
            + System.lineSeparator()
            + "Seats " + "(" + SEAT_1.getRowNum() + "," + SEAT_1.getColumnNum() + "), "
            + "(" + SEAT_2.getRowNum() + "," + SEAT_2.getColumnNum() + ") "
            + "on " + MOVIE_TITLE + " in room " + ROOM_NAME + " starting at " + mapDateToString(TIME)
            + " for " + PRICE + " HUF";
    private static final String NON_PRIVILEGED_LOGIN_WITHOUT_BOOKINGS = "Signed in with account '"
            + USERNAME
            + "'"
            + System.lineSeparator()
            + "You have not booked any tickets yet";
    private static final String PRIVILEGED_LOGIN = String.format("Signed in with privileged account '%s'",
            USERNAME);

    private static String mapDateToString(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(dateTimeFormatter);
    }

    @Test
    void testSignInPrivilegedWithoutError() throws SignInFailedException {
        //When
        String actual = accountController.signInPrivileged(USERNAME, PASSWORD);

        //Then
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(accountService, times(1)).signInAccountPrivileged(usernameCaptor.capture(),
                passwordCaptor.capture());
        assertThat(usernameCaptor.getValue(), equalTo(USERNAME));
        assertThat(passwordCaptor.getValue(), equalTo(PASSWORD));
        assertThat(actual, equalTo(SUCCESS));
    }

    @Test
    void testSignInPrivilegedWithSignInFailedException() throws SignInFailedException {
        //When
        doThrow(SignInFailedException.class)
                .when(accountService)
                .signInAccountPrivileged(any(), any());
        String actual = accountController.signInPrivileged(USERNAME, PASSWORD);

        //Then
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(accountService, times(1)).signInAccountPrivileged(usernameCaptor.capture(),
                passwordCaptor.capture());
        assertThat(usernameCaptor.getValue(), equalTo(USERNAME));
        assertThat(passwordCaptor.getValue(), equalTo(PASSWORD));
        assertThat(actual, equalTo(SIGN_IN_FAIL));
    }

    @Test
    void testSignOutWithoutError() throws NoSignedInAccountException {
        //When
        String actual = accountController.signOut();

        //Then
        verify(accountService, times(1)).signOutAccount();
        assertThat(actual, equalTo(SUCCESS));
    }

    @Test
    void testSignOutWithNoSignedInAccountException() throws NoSignedInAccountException {
        doThrow(NoSignedInAccountException.class)
                .when(accountService)
                .signOutAccount();

        //When
        String actual = accountController.signOut();

        //Then
        verify(accountService, times(1)).signOutAccount();
        assertThat(actual, equalTo(NO_SIGNED_IN_ACCOUNT));
    }


    @Test
    void testDescribeAccountWithoutSignedInAccount() throws NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenThrow(new NoSignedInAccountException(""));

        //When
        String actual = accountController.describeAccount();

        //Then
        assertThat(actual, equalTo(NO_SIGNED_IN_ACCOUNT));
    }

    @Test
    void testDescribeAccountWithPrivilegedAccount() throws NoSignedInAccountException, PrivilegedAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenReturn(PRIVILEGED_ACCOUNT);
        when(accountService.getBookingsOfSignedInAccount()).thenThrow(new PrivilegedAccountException(""));

        //When
        String actual = accountController.describeAccount();

        //Then
        assertThat(actual, equalTo(PRIVILEGED_LOGIN));
    }

    @Test
    void testDescribeAccountWithSignedInAccountWithBookings() throws NoSignedInAccountException,
            PrivilegedAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);
        when(accountService.getBookingsOfSignedInAccount()).thenReturn(List.of(BOOKING));

        //When
        String actual = accountController.describeAccount();

        //Then
        assertThat(actual, equalTo(NON_PRIVILEGED_LOGIN_WITH_BOOKINGS));
    }

    @Test
    void testDescribeAccountWithSignedInAccountWithoutBookings() throws NoSignedInAccountException,
            PrivilegedAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);
        when(accountService.getBookingsOfSignedInAccount()).thenReturn(List.of());

        //When
        String actual = accountController.describeAccount();

        //Then
        assertThat(actual, equalTo(NON_PRIVILEGED_LOGIN_WITHOUT_BOOKINGS));
    }

    @Test
    void testSignUpAccountWithoutError() throws AccountAlreadyExistsException {
        //When
        String actual = accountController.signUpAccount(USERNAME, PASSWORD);

        //Then
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(accountService, times(1)).signUpAccount(usernameCaptor.capture(),
                passwordCaptor.capture());
        assertThat(usernameCaptor.getValue(), equalTo(USERNAME));
        assertThat(passwordCaptor.getValue(), equalTo(PASSWORD));
        assertThat(actual, equalTo(SUCCESS));
    }

    @Test
    void testSignUpAccountWithAccountAlreadyExistsException() throws AccountAlreadyExistsException {
        //Given
        doThrow(AccountAlreadyExistsException.class)
                .when(accountService)
                .signUpAccount(any(), any());

        //When
        String actual = accountController.signUpAccount(USERNAME, PASSWORD);

        //Then
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(accountService, times(1)).signUpAccount(usernameCaptor.capture(),
                passwordCaptor.capture());
        assertThat(usernameCaptor.getValue(), equalTo(USERNAME));
        assertThat(passwordCaptor.getValue(), equalTo(PASSWORD));
        assertThat(actual, equalTo(SIGN_UP_FAIL));
    }

    @Test
    void testSignInWithoutError() throws SignInFailedException {
        //When
        String actual = accountController.signIn(USERNAME, PASSWORD);

        //Then
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(accountService, times(1)).signInAccount(usernameCaptor.capture(),
                passwordCaptor.capture());
        assertThat(usernameCaptor.getValue(), equalTo(USERNAME));
        assertThat(passwordCaptor.getValue(), equalTo(PASSWORD));
        assertThat(actual, equalTo(SUCCESS));
    }

    @Test
    void testSignInWithSignInFailedException() throws SignInFailedException {
        //When
        doThrow(SignInFailedException.class)
                .when(accountService)
                .signInAccount(any(), any());
        String actual = accountController.signIn(USERNAME, PASSWORD);

        //Then
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(accountService, times(1)).signInAccount(usernameCaptor.capture(),
                passwordCaptor.capture());
        assertThat(usernameCaptor.getValue(), equalTo(USERNAME));
        assertThat(passwordCaptor.getValue(), equalTo(PASSWORD));
        assertThat(actual, equalTo(SIGN_IN_FAIL));
    }

}