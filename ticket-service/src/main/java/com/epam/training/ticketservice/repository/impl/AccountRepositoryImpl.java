package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.AccountDao;
import com.epam.training.ticketservice.dataccess.entity.AccountEntity;
import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.repository.AccountRepository;
import com.epam.training.ticketservice.repository.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.AccountNotFoundException;
import com.epam.training.ticketservice.repository.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountDao accountDao;
    private final AccountMapper accountMapper;

    @Override
    public void createAccount(Account account) throws AccountAlreadyExistsException {
        Optional<AccountEntity> accountEntity = accountDao.findById(account.getUsername());
        if (accountEntity.isPresent()) {
            throw new AccountAlreadyExistsException(String.format("Account already exists: %s",
                    account.getUsername()));
        }
        accountDao.save(accountMapper.mapToAccountEntity(account));
    }

    @Override
    public Account getAccountByUserName(String username) throws AccountNotFoundException {
        Optional<AccountEntity> accountEntity = accountDao.findById(username);
        if (accountEntity.isEmpty()) {
            throw new AccountNotFoundException(String.format("Username not found: %s", username));
        }
        return accountMapper.mapToAccount(accountEntity.get());
    }
}
