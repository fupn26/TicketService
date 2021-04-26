package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dataccess.entity.AccountEntity;
import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.repository.mapper.AccountMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account mapToAccount(AccountEntity accountEntityToMap) {
        return new Account(accountEntityToMap.getUsername(),
                accountEntityToMap.getPassword(),
                accountEntityToMap.isPrivileged());
    }

    @Override
    public AccountEntity mapToAccountEntity(Account accountToMap) {
        return new AccountEntity(accountToMap.getUsername(),
                accountToMap.getPassword(),
                accountToMap.isPrivileged());
    }
}
