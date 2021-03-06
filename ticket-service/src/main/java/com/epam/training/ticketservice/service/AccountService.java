package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.repository.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import com.epam.training.ticketservice.service.exception.PrivilegedAccountException;
import com.epam.training.ticketservice.service.exception.SignInFailedException;

import java.util.List;

public interface AccountService {
    void signUpAccount(String username, String password) throws AccountAlreadyExistsException;

    void signInAccountPrivileged(String username, String password) throws SignInFailedException;

    void signInAccount(String username, String password) throws SignInFailedException;

    void signOutAccount() throws NoSignedInAccountException;

    Account getSignedInAccount() throws NoSignedInAccountException;

    List<Booking> getBookingsOfSignedInAccount() throws NoSignedInAccountException, PrivilegedAccountException;
}
