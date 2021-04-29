package com.epam.training.ticketservice.service.exception;

public class SeatDuplicatedException extends Exception {
    public SeatDuplicatedException(String message) {
        super(message);
    }
}
