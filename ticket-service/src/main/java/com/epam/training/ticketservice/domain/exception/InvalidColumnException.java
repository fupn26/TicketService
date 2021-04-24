package com.epam.training.ticketservice.domain.exception;

public class InvalidColumnException extends Exception {
    public InvalidColumnException(String message) {
        super(message);
    }
}
