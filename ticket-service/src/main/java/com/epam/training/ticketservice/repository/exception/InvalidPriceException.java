package com.epam.training.ticketservice.repository.exception;

public class InvalidPriceException extends Exception {
    public InvalidPriceException(String message) {
        super(message);
    }
}
