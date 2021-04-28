package com.epam.training.ticketservice.controller.availability;

import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import org.springframework.shell.Availability;
import org.springframework.stereotype.Service;

@Service
public class ShellMethodAvailabilityCheckerImpl implements ShellMethodAvailabilityChecker {

    private final AccountService accountService;
    private final String privilegedAccountMessage = "you are a privileged user";
    private final String noPrivilegedAccountMessage = "you are not a privileged user";
    private final String noSignedInAccountMessage = "you are not signed in";

    public ShellMethodAvailabilityCheckerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Availability isPrivilegedAccountSignIn() {
        try {
            Account account = accountService.getSignedInAccount();
            return account.isPrivileged()
                    ? Availability.available()
                    : Availability.unavailable(noPrivilegedAccountMessage);
        } catch (NoSignedInAccountException e) {
            return Availability.unavailable(noSignedInAccountMessage);
        }
    }

    @Override
    public Availability isNonPrivilegedAccountSignIn() {
        try {
            Account account = accountService.getSignedInAccount();
            return account.isPrivileged()
                    ? Availability.unavailable(privilegedAccountMessage)
                    : Availability.available();
        } catch (NoSignedInAccountException e) {
            return Availability.unavailable(noSignedInAccountMessage);
        }
    }
}
