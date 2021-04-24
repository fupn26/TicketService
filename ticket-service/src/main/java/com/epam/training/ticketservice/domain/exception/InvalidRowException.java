package com.epam.training.ticketservice.domain.exception;

public class InvalidRowException extends Exception {
    public InvalidRowException(String message) {
        super(message);
    }
}
