package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.repository.AccountRepository;
import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.repository.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.AccountNotFoundException;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.BookingService;
import com.epam.training.ticketservice.service.exception.InvalidPasswordException;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import com.epam.training.ticketservice.service.exception.PrivilegedAccountException;
import com.epam.training.ticketservice.service.exception.SignInFailedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final BookingRepository bookingRepository;
    private final String noSignedInUserMessage = "No signed in account";
    private final String incorrectPasswordMessage = "The given password is incorrect";
    private final String notPrivilegedMessage = "Account with username %s is not privileged";
    private final String privilegedMessage = "Account with username %s is privileged";
    private Account currentAccount;

    public AccountServiceImpl(AccountRepository accountRepository, BookingRepository bookingRepository) {
        this.accountRepository = accountRepository;
        this.bookingRepository = bookingRepository;
        currentAccount = null;
    }

    @Override
    public void signUpAccount(String username, String password) throws AccountAlreadyExistsException {
        accountRepository.createAccount(new Account(username, password, false));
    }

    @Override
    public void signInAccountPrivileged(String username, String password) throws SignInFailedException {
        try {
            Account account = accountRepository.getAccountByUserName(username);
            validatePrivilegedAccount(account, password);
            currentAccount = account;
        } catch (AccountNotFoundException | PrivilegedAccountException | InvalidPasswordException e) {
            throw new SignInFailedException(e.getMessage());
        }
    }

    private void validatePrivilegedAccount(Account accountToValidate, String passwordToValidate)
            throws PrivilegedAccountException, InvalidPasswordException {
        if (!accountToValidate.isPrivileged()) {
            throw new PrivilegedAccountException(String.format(notPrivilegedMessage,
                    accountToValidate.getUsername()));
        }
        if (!accountToValidate.getPassword().equals(passwordToValidate)) {
            throw new InvalidPasswordException(incorrectPasswordMessage);
        }
    }

    @Override
    public void signInAccount(String username, String password) throws SignInFailedException {
        try {
            Account account = accountRepository.getAccountByUserName(username);
            validateAccount(account, password);
            currentAccount = account;
        } catch (AccountNotFoundException | PrivilegedAccountException | InvalidPasswordException e) {
            throw new SignInFailedException(e.getMessage());
        }
    }

    private void validateAccount(Account accountToValidate, String passwordToValidate)
            throws PrivilegedAccountException, InvalidPasswordException {
        if (accountToValidate.isPrivileged()) {
            throw new PrivilegedAccountException(String.format(privilegedMessage,
                    accountToValidate.getUsername()));
        }
        if (!accountToValidate.getPassword().equals(passwordToValidate)) {
            throw new InvalidPasswordException(incorrectPasswordMessage);
        }
    }

    @Override
    public void signOutAccount() throws NoSignedInAccountException {
        if (currentAccount == null) {
            throw new NoSignedInAccountException(noSignedInUserMessage);
        }
        currentAccount = null;
    }

    @Override
    public Account getSignedInAccount() throws NoSignedInAccountException {
        if (currentAccount == null) {
            throw new NoSignedInAccountException(noSignedInUserMessage);
        }
        return currentAccount;
    }

    @Override
    public List<Booking> getBookingsOfSignedInAccount() throws NoSignedInAccountException, PrivilegedAccountException {
        if (currentAccount == null) {
            throw new NoSignedInAccountException(noSignedInUserMessage);
        }
        if (currentAccount.isPrivileged()) {
            throw new PrivilegedAccountException(String.format(privilegedMessage, currentAccount.getUsername()));
        }
        return bookingRepository.getBookingsByUserName(currentAccount.getUsername());
    }
}
