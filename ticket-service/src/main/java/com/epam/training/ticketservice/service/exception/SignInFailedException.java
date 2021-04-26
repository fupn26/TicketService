package com.epam.training.ticketservice.service.exception;

public class SignInFailedException extends Exception {
    public SignInFailedException(String message) {
        super(message);
    }
}
