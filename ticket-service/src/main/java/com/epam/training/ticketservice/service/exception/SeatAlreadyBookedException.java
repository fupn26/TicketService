package com.epam.training.ticketservice.service.exception;

import com.epam.training.ticketservice.domain.Seat;
import lombok.Getter;

public class SeatAlreadyBookedException extends Exception {
    @Getter
    private final Seat seat;

    public SeatAlreadyBookedException(String message, Seat seat) {
        super(message);
        this.seat = seat;
    }
}
