package com.epam.training.ticketservice.repository.exception;

import lombok.Getter;

@Getter
public class SeatNotFoundException extends Exception {
    private final String roomName;
    private final int row;
    private final int column;

    public SeatNotFoundException(String message, String roomName, int row, int column) {
        super(message);
        this.roomName = roomName;
        this.row = row;
        this.column = column;
    }
}
