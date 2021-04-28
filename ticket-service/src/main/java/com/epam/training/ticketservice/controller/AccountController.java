package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.repository.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import com.epam.training.ticketservice.service.exception.PrivilegedAccountException;
import com.epam.training.ticketservice.service.exception.SignInFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;

@Slf4j
@ShellComponent
public class AccountController {

    private final AccountService accountService;
    private final String loginFail = "Login failed due to incorrect credentials";
    private final String noSignedInAccount = "You are not signed in";
    private final String privilegedAccount = "Signed in with privileged account '%s'";
    private final String nonPrivilegedAccount = "Signed in with account '%s'";
    private final String noBookings = "You have not booked any tickets yet";
    private final String bookingsExists = "Your previous bookings are";
    private final String bookingEntry = "Seats %s on %s in room %s starting at %s for %d HUF";
    private final String seatEntry = "(%d,%d)";
    private final String dateTimeFormat = "yyyy-MM-dd HH:mm";
    private final String accountAlreadyExists = "Account with username '%s' already exists";

    private AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ShellMethod(value = "Signs in the user with admin privileges", key = "sign in privileged")
    public String signInPrivileged(String username, String password) {
        String result = null;
        try {
            accountService.signInAccountPrivileged(username, password);
        } catch (SignInFailedException e) {
            result = loginFail;
        }
        return result;
    }

    @ShellMethod(value = "Signs in the user", key = "sign in")
    public String signIn(String username, String password) {
        String result = null;
        try {
            accountService.signInAccount(username, password);
        } catch (SignInFailedException e) {
            result = loginFail;
        }
        return result;
    }

    @ShellMethod(value = "Signs out the user", key = "sign out")
    public String signOut() {
        String result = null;
        try {
            accountService.signOutAccount();
        } catch (NoSignedInAccountException e) {
            result = noSignedInAccount;
        }
        return result;
    }


    @ShellMethod(value = "Describes the current user", key = "describe account")
    public String describeAccount() {
        String result = null;
        Account signedInAccount = null;
        try {
            signedInAccount = accountService.getSignedInAccount();
            result = createAccountDescription(signedInAccount);
        } catch (NoSignedInAccountException e) {
            result = noSignedInAccount;
        }
        return result;
    }

    @ShellMethod(value = "Signs up the user", key = "sign up")
    public String signUpAccount(String username, String password) {
        String result = null;
        try {
            accountService.signUpAccount(username, password);
        } catch (AccountAlreadyExistsException e) {
            result = String.format(accountAlreadyExists, username);
        }
        return result;
    }

    private String createAccountDescription(Account account) {
        StringBuilder result = new StringBuilder();
        List<Booking> bookings = null;
        try {
            bookings = accountService.getBookingsOfSignedInAccount();
            result.append(String.format(nonPrivilegedAccount, account.getUsername()));
            if (bookings.isEmpty()) {
                result.append(System.lineSeparator());
                result.append(noBookings);
            } else {
                result.append(System.lineSeparator());
                result.append(bookingsExists)
                        .append(System.lineSeparator())
                        .append(bookings.stream()
                                .map(this::mapBookingToString)
                                .collect(joining(System.lineSeparator()))
                    );
            }
        } catch (NoSignedInAccountException e) {
            log.warn(e.getMessage());
        } catch (PrivilegedAccountException e) {
            result.append(String.format(privilegedAccount, account.getUsername()));
        }
        return result.toString();
    }

    private String mapBookingToString(Booking bookingToMap) {
        return String.format(bookingEntry,
                mapSeatsToString(bookingToMap.getSeatList()),
                bookingToMap.getScreening().getMovie().getTitle(),
                bookingToMap.getScreening().getRoom().getName(),
                mapDateTimeToString(bookingToMap.getScreening().getStartDate()),
                bookingToMap.getPrice()
                );
    }

    private String mapSeatToString(Seat seat) {
        return String.format(seatEntry, seat.getRowNum(), seat.getColumnNum());
    }

    private String mapSeatsToString(Set<Seat> seats) {
        return seats.stream()
                .map(this::mapSeatToString)
                .collect(joining(", "));
    }

    private String mapDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        return dateTime.format(dateTimeFormatter);
    }
}
