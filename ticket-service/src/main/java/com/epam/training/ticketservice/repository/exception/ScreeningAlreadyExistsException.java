package com.epam.training.ticketservice.repository.exception;

public class ScreeningAlreadyExistsException extends Exception {
    public ScreeningAlreadyExistsException(String message) {
        super(message);
    }
}
