package com.epam.training.ticketservice.service.exception;

public class NoSignedInAccountException extends Exception {
    public NoSignedInAccountException(String message) {
        super(message);
    }
}
