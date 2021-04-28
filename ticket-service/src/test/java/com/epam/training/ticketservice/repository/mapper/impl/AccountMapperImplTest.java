package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dataccess.entity.AccountEntity;
import com.epam.training.ticketservice.domain.Account;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class AccountMapperImplTest {

    private static final String USERNAME = "james";
    private static final String PASSWORD = "bond";
    private static final boolean IS_PRIVILEGED = true;
    private static final Account ACCOUNT = new Account(USERNAME, PASSWORD, IS_PRIVILEGED);
    private static final AccountEntity ACCOUNT_ENTITY = new AccountEntity(USERNAME, PASSWORD, IS_PRIVILEGED);

    private final AccountMapperImpl accountMapper = new AccountMapperImpl();

    @Test
    void testMapToAccount() {
        //When
        Account actual = accountMapper.mapToAccount(ACCOUNT_ENTITY);

        //Then
        assertThat(actual, equalTo(ACCOUNT));
    }

    @Test
    void testMapToAccountEntity() {
        //When
        AccountEntity actual = accountMapper.mapToAccountEntity(ACCOUNT);

        //Then
        assertThat(actual, equalTo(ACCOUNT_ENTITY));
    }
}