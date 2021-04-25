package com.epam.training.ticketservice.domain.exception;

public class PriceComponentAlreadyAttachedException extends Exception {
    public PriceComponentAlreadyAttachedException(String message) {
        super(message);
    }
}
