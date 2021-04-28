package com.epam.training.ticketservice.repository.exception;

public class SeatAlreadyExistsException extends Exception {
    public SeatAlreadyExistsException(String message) {
        super(message);
    }
}
