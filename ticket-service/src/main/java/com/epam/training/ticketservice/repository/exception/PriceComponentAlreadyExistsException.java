package com.epam.training.ticketservice.repository.exception;

public class PriceComponentAlreadyExistsException extends Exception {
    public PriceComponentAlreadyExistsException(String message) {
        super(message);
    }
}
