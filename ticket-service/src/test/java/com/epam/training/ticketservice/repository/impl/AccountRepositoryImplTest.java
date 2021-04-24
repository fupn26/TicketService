package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dao.AccountDao;
import com.epam.training.ticketservice.dao.entity.AccountEntity;
import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.repository.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryImplTest {

    @Spy
    @InjectMocks
    private AccountRepositoryImpl accountRepository;
    @Mock
    private AccountDao accountDao;

    private static final String USERNAME = "james";
    private static final Account ACCOUNT = new Account(USERNAME, "bond", false);
    private static final AccountEntity ACCOUNT_ENTITY = new AccountEntity(USERNAME, "bond", false);

    @Test
    void testCreateAccountWithoutError() throws AccountAlreadyExistsException {
        //Given
        when(accountDao.findById(any())).thenReturn(Optional.empty());

        //When
        accountRepository.createAccount(ACCOUNT);

        //Then
        ArgumentCaptor<AccountEntity> accountEntityArgumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(accountDao, times(1)).save(accountEntityArgumentCaptor.capture());
        assertThat(ACCOUNT_ENTITY, equalTo(accountEntityArgumentCaptor.getValue()));
    }

    @Test
    void testCreateAccountWithAccountAlreadyExistsException() {
        //Given
        when(accountDao.findById(any())).thenReturn(Optional.of(ACCOUNT_ENTITY));

        //Then
        assertThrows(AccountAlreadyExistsException.class, () -> {
            //When
            accountRepository.createAccount(ACCOUNT);
        });
    }

    @Test
    void testGetAccountByUserNameWithoutError() throws AccountNotFoundException {
        //Given
        when(accountDao.findById(any())).thenReturn(Optional.of(ACCOUNT_ENTITY));

        //When
        Account account = accountRepository.getAccountByUserName(USERNAME);

        //Then
        assertThat(ACCOUNT, equalTo(account));
    }

    @Test
    void testGetAccountByUserNameWithAccountNotFoundException() {
        //Given
        when(accountDao.findById(any())).thenReturn(Optional.empty());

        //Then
        assertThrows(AccountNotFoundException.class, () -> {
            //When
            accountRepository.getAccountByUserName(USERNAME);
        });
    }
}