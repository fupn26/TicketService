package com.epam.training.ticketservice.domain.exception;

public class PriceComponentAlreadyAddedException extends Exception {
    public PriceComponentAlreadyAddedException(String message) {
        super(message);
    }
}
