package com.epam.training.ticketservice.controller.availability;

import org.springframework.shell.Availability;

public interface ShellMethodAvailabilityChecker {
    Availability isPrivilegedAccountSignIn();

    Availability isNonPrivilegedAccountSignIn();
}
