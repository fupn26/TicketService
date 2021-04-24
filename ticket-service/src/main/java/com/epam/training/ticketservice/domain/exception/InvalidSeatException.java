package com.epam.training.ticketservice.domain.exception;

public class InvalidSeatException extends Exception {
    public InvalidSeatException(String message) {
        super(message);
    }
}
