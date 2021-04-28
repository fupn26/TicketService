package com.epam.training.ticketservice.repository.exception;

public class SeatNotFoundException extends Exception {
    public SeatNotFoundException(String message) {
        super(message);
    }
}
