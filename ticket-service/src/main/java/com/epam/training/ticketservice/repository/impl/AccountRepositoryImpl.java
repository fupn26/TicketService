package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dao.AccountDao;
import com.epam.training.ticketservice.dao.entity.AccountEntity;
import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.repository.AccountRepository;
import com.epam.training.ticketservice.repository.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountDao accountDao;

    @Override
    public void createAccount(Account account) throws AccountAlreadyExistsException {
        Optional<AccountEntity> accountEntity = accountDao.findById(account.getUsername());
        if (accountEntity.isPresent()) {
            throw new AccountAlreadyExistsException(String.format("Account already exists: %s",
                    account.getUsername()));
        }
        accountDao.save(mapToAccountEntity(account));
    }

    @Override
    public Account getAccountByUserName(String username) throws AccountNotFoundException {
        Optional<AccountEntity> accountEntity = accountDao.findById(username);
        if (accountEntity.isEmpty()) {
            throw new AccountNotFoundException(String.format("Username not found: %s", username));
        }
        return mapToAccount(accountEntity.get());
    }

    private Account mapToAccount(AccountEntity accountEntity) {
        return new Account(accountEntity.getUsername(),
                accountEntity.getPassword(),
                accountEntity.isPrivileged());
    }

    private AccountEntity mapToAccountEntity(Account account) {
        return new AccountEntity(account.getUsername(),
                account.getPassword(),
                account.isPrivileged());
    }
}
