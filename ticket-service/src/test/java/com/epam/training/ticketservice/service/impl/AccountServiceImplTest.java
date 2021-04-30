package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.*;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.domain.exception.InvalidSeatException;
import com.epam.training.ticketservice.repository.AccountRepository;
import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.repository.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.AccountNotFoundException;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BookingRepository bookingRepository;

    private static final String USERNAME = "james";
    private static final String PASSWORD = "bond";
    private static final String INVALID_PASSWORD = "bondd";
    private static final Account ACCOUNT = new Account(USERNAME, PASSWORD, false);
    private static final Account PRIVILEGED_ACCOUNT = new Account(USERNAME, PASSWORD, true);
    private static final String MOVIE_TITLE = "best movie";
    private static final String MOVIE_GENRE = "drama";
    private static final int MOVIE_LENGTH = 100;
    private static final String ROOM_NAME = "best room";
    private static final int ROWS = 10;
    private static final int COLUMNS = 20;
    private static final LocalDateTime TIME = LocalDateTime.now();

    private static final String PRICE_COMPONENT_NAME = "glamour";
    private static final int PRICE_COMPONENT_VALUE = -200;
    private static final int BASE_PRICE = 1500;

    private static final PriceComponent PRICE_COMPONENT = new PriceComponent(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponent> PRICE_COMPONENTS = Set.of(PRICE_COMPONENT);

    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, PRICE_COMPONENTS);

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(AccountServiceImplTest.MOVIE_TITLE, genre, length, priceComponents);
        } catch (InvalidMovieLengthException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final Room ROOM = createRoom(ROOM_NAME, ROWS, COLUMNS, PRICE_COMPONENTS);

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
    private static final Screening SCREENING = new Screening(ID, MOVIE, ROOM, TIME, PRICE_COMPONENTS);

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
        return new LinkedHashSet<>(seatList);
    }

    private static final Booking BOOKING = createBooking(SCREENING, ACCOUNT, SEATS, BASE_PRICE);
    private static final List<Booking> BOOKINGS = List.of(BOOKING, BOOKING);

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
    void testSignUpAccountWithNonExistingAccount() throws AccountAlreadyExistsException {
        //When
        accountService.signUpAccount(USERNAME, PASSWORD);

        //Then
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).createAccount(accountArgumentCaptor.capture());
        Account actual = accountArgumentCaptor.getValue();
        assertThat(actual, equalTo(ACCOUNT));
    }

    @Test
    void testSignUpAccountWithExistingAccountThrowAccountAlreadyExistsException()
            throws AccountAlreadyExistsException {
        //Given
        doThrow(AccountAlreadyExistsException.class)
                .when(accountRepository)
                .createAccount(any());

        //Then
        assertThrows(AccountAlreadyExistsException.class, () -> {
            //When
            accountService.signUpAccount(USERNAME, PASSWORD);
        });
    }

    @Test
    void testSignInAccountPrivilegedWithExistingPrivilegedAccount()
            throws AccountNotFoundException, SignInFailedException, NoSignedInAccountException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenReturn(PRIVILEGED_ACCOUNT);

        //When
        accountService.signInAccountPrivileged(USERNAME, PASSWORD);

        //Then
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        verify(accountRepository, times(1)).getAccountByUserName(usernameCaptor.capture());
        String actual = usernameCaptor.getValue();
        assertThat(actual, equalTo(USERNAME));
        assertThat(accountService.getSignedInAccount(), equalTo(PRIVILEGED_ACCOUNT));
    }

    @Test
    void testSignInAccountPrivilegedWithNonExistingAccountThrowSignInFailedException()
            throws AccountNotFoundException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenThrow(AccountNotFoundException.class);

        //Then
        assertThrows(SignInFailedException.class, () -> {
            //When
            accountService.signInAccountPrivileged(USERNAME, PASSWORD);
        });
    }

    @Test
    void testSignInAccountPrivilegedWithExistingNonPrivilegedAccountThrowSignInFailedException()
            throws AccountNotFoundException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenReturn(ACCOUNT);

        //Then
        assertThrows(SignInFailedException.class, () -> {
            //When
            accountService.signInAccountPrivileged(USERNAME, PASSWORD);
        });
    }

    @Test
    void testSignInAccountPrivilegedWithExistingPrivilegedAccountAndInvalidPasswordThrowSignInFailedException()
            throws AccountNotFoundException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenReturn(PRIVILEGED_ACCOUNT);

        //Then
        assertThrows(SignInFailedException.class, () -> {
            //When
            accountService.signInAccountPrivileged(USERNAME, INVALID_PASSWORD);
        });
    }

    @Test
    void testSignInAccountWithExistingNonPrivilegedAccount() throws AccountNotFoundException,
            SignInFailedException, NoSignedInAccountException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenReturn(ACCOUNT);

        //When
        accountService.signInAccount(USERNAME, PASSWORD);

        //Then
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        verify(accountRepository, times(1)).getAccountByUserName(usernameCaptor.capture());
        String actual = usernameCaptor.getValue();
        assertThat(actual, equalTo(USERNAME));
        assertThat(accountService.getSignedInAccount(), equalTo(ACCOUNT));
    }

    @Test
    void testSignInAccountWithExistingPrivilegedAccountThrowSignInFailedException()
            throws AccountNotFoundException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenReturn(PRIVILEGED_ACCOUNT);

        //Then
        assertThrows(SignInFailedException.class, () -> {
            //When
            accountService.signInAccount(USERNAME, PASSWORD);
        });
    }

    @Test
    void testSignInAccountWithNonExistingAccountThrowSignInFailedException()
            throws AccountNotFoundException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenThrow(AccountNotFoundException.class);

        //Then
        assertThrows(SignInFailedException.class, () -> {
            //When
            accountService.signInAccount(USERNAME, PASSWORD);
        });
    }

    @Test
    void testSignInAccountWithExistingNonPrivilegedAccountWithInvalidPasswordPaThrowSignInFailedException()
            throws AccountNotFoundException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenReturn(ACCOUNT);

        //Then
        assertThrows(SignInFailedException.class, () -> {
            //When
            accountService.signInAccount(USERNAME, INVALID_PASSWORD);
        });
    }

    @Test
    void testSignOutAccountWithAlreadySignInAccount() throws SignInFailedException,
            AccountNotFoundException, NoSignedInAccountException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenReturn(ACCOUNT);
        accountService.signInAccount(USERNAME, PASSWORD);

        //When
        accountService.signOutAccount();

        //Then
        assertThrows(NoSignedInAccountException.class, () -> accountService.getSignedInAccount());
    }

    @Test
    void testSignOutAccountWithoutAlreadySignInAccountThrowNoSignedInAccountException() {
        //Then
        assertThrows(NoSignedInAccountException.class, () -> {
            //When
            accountService.signOutAccount();
        });
    }

    @Test
    void testGetSignedInAccountWithAlreadySignedInAccount()
            throws AccountNotFoundException, SignInFailedException, NoSignedInAccountException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenReturn(ACCOUNT);
        accountService.signInAccount(USERNAME, PASSWORD);

        //When
        Account actual = accountService.getSignedInAccount();

        //Then
        assertThat(actual, equalTo(ACCOUNT));
    }

    @Test
    void testGetSignedInAccountWithoutAlreadySignedInAccount() {
        //Then
        assertThrows(NoSignedInAccountException.class, () -> {
            //When
            accountService.getSignedInAccount();
        });
    }

    @Test
    void testGetBookingsOfSignedInAccountWithAlreadySignedInNonPrivilegedAccount()
            throws PrivilegedAccountException, NoSignedInAccountException,
            AccountNotFoundException, SignInFailedException {
        //Given
        when(bookingRepository.getBookingsByUserName(USERNAME)).thenReturn(BOOKINGS);
        when(accountRepository.getAccountByUserName(any())).thenReturn(ACCOUNT);
        accountService.signInAccount(USERNAME, PASSWORD);

        //When
        List<Booking> actual = accountService.getBookingsOfSignedInAccount();

        //Then
        assertThat(actual, equalTo(BOOKINGS));
    }

    @Test
    void testGetBookingsOfSignedInAccountWithAlreadySignedInPrivilegedAccountThrowPrivilegedAccountException()
            throws AccountNotFoundException, SignInFailedException {
        //Given
        when(accountRepository.getAccountByUserName(any())).thenReturn(PRIVILEGED_ACCOUNT);
        accountService.signInAccountPrivileged(USERNAME, PASSWORD);

        //Then
        assertThrows(PrivilegedAccountException.class, () -> {
            //When
           accountService.getBookingsOfSignedInAccount();
        });
    }

    @Test
    void testGetBookingsOfSignedInAccountWithoutAlreadySignedInNonPrivilegedAccountThrowNoSignedInAccountException() {
        //Then
        assertThrows(NoSignedInAccountException.class, () -> {
            //When
            accountService.getBookingsOfSignedInAccount();
        });
    }

}
