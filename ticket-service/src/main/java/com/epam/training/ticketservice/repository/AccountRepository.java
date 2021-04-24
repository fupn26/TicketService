package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.repository.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.AccountNotFoundException;

import java.util.Optional;

public interface AccountRepository {
    void createAccount(Account account) throws AccountAlreadyExistsException;
    Account getAccountByUserName(String username) throws AccountNotFoundException;
}
