package com.epam.training.ticketservice.domain.exception;

public class InvalidMovieLengthException extends Exception {
    public InvalidMovieLengthException(String message) {
        super(message);
    }
}
