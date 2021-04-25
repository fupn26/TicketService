package com.epam.training.ticketservice.repository.exception;

public class RoomAlreadyExistsException extends Exception {
    public RoomAlreadyExistsException(String message) {
        super(message);
    }
}
