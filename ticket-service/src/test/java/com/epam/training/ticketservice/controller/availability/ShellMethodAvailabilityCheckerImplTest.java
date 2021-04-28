package com.epam.training.ticketservice.controller.availability;

import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.shell.Availability;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShellMethodAvailabilityCheckerImplTest {

    @InjectMocks
    private ShellMethodAvailabilityCheckerImpl shellMethodAvailabilityChecker;
    @Mock
    private AccountService accountService;

    private static final String ACCOUNT_NAME = "james";
    private static final String ACCOUNT_PASSWORD = "bond";
    private static final String NO_PRIVILEGED_ACCOUNT_MESSAGE = "you are not a privileged user";
    private static final String PRIVILEGED_ACCOUNT_MESSAGE = "you are a privileged user";
    private static final String NO_SIGNED_IN_ACCOUNT_MESSAGE = "you are not signed in";
    private static final Account ACCOUNT = new Account(ACCOUNT_NAME, ACCOUNT_PASSWORD, false);
    private static final Account ACCOUNT_PRIVILEGED = new Account(ACCOUNT_NAME, ACCOUNT_PASSWORD, true);

    @Test
    void testIsPrivilegedUserSignInWithSignedInAccountPrivileged() throws NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT_PRIVILEGED);

        //When
        Availability actual = shellMethodAvailabilityChecker.isPrivilegedAccountSignIn();

        //Then
        verify(accountService, times(1)).getSignedInAccount();
        assertThat(actual.isAvailable(), equalTo(true));
    }

    @Test
    void testIsPrivilegedUserSignInWithSignedInAccountNonPrivileged() throws NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);

        //When
        Availability actual = shellMethodAvailabilityChecker.isPrivilegedAccountSignIn();

        //Then
        verify(accountService, times(1)).getSignedInAccount();
        assertThat(actual.getReason(), equalTo(NO_PRIVILEGED_ACCOUNT_MESSAGE));
    }

    @Test
    void testIsPrivilegedUserSignInWithoutSignedInAccount() throws NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenThrow(NoSignedInAccountException.class);

        //When
        Availability actual = shellMethodAvailabilityChecker.isPrivilegedAccountSignIn();

        //Then
        verify(accountService, times(1)).getSignedInAccount();
        assertThat(actual.getReason(), equalTo(NO_SIGNED_IN_ACCOUNT_MESSAGE));
    }

    @Test
    void testIsNonPrivilegedAccountSignInWithSignInNonPrivilegedAccount() throws NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);

        //When
        Availability actual = shellMethodAvailabilityChecker.isNonPrivilegedAccountSignIn();

        //Then
        verify(accountService, times(1)).getSignedInAccount();
        assertThat(actual.isAvailable(), equalTo(true));
    }

    @Test
    void testIsNonPrivilegedAccountSignInWithSignInPrivilegedAccount() throws NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT_PRIVILEGED);

        //When
        Availability actual = shellMethodAvailabilityChecker.isNonPrivilegedAccountSignIn();

        //Then
        verify(accountService, times(1)).getSignedInAccount();
        assertThat(actual.getReason(), equalTo(PRIVILEGED_ACCOUNT_MESSAGE));
    }

    @Test
    void testIsNonPrivilegedAccountSignInWithoutSignInAccountThrowsNoSignedInAccountException()
            throws NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenThrow(NoSignedInAccountException.class);

        //When
        Availability actual = shellMethodAvailabilityChecker.isNonPrivilegedAccountSignIn();

        //Then
        verify(accountService, times(1)).getSignedInAccount();
        assertThat(actual.getReason(), equalTo(NO_SIGNED_IN_ACCOUNT_MESSAGE));
    }
}