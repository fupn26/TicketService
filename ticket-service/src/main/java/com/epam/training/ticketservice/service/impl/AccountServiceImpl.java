package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.repository.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import com.epam.training.ticketservice.service.exception.PrivilegedAccountException;
import com.epam.training.ticketservice.service.exception.SignInFailedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Override
    public void signUpAccount(String username, String password) throws AccountAlreadyExistsException {

    }

    @Override
    public void signInAccountPrivileged(String username, String password) throws SignInFailedException {

    }

    @Override
    public void signOutAccount() {

    }

    @Override
    public Account getSignedInAccount() throws NoSignedInAccountException {
        return null;
    }

    @Override
    public List<Booking> getBookingsOfSignedInAccount() throws NoSignedInAccountException, PrivilegedAccountException {
        return null;
    }
}
