package com.epam.training.ticketservice.repository.exception;

public class MovieMalformedException extends Exception {
    public MovieMalformedException(String message) {
        super(message);
    }
}
