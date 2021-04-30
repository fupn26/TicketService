package com.epam.training.ticketservice.domain;

import com.epam.training.ticketservice.domain.exception.InvalidSeatException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class Booking {
    private final Screening screening;
    private final Account account;
    private final LinkedHashSet<Seat> seatList;
    private final int price;

    public Booking(Screening screening, Account account, LinkedHashSet<Seat> seatList, int price)
            throws InvalidSeatException {
        validateSeats(screening, seatList);

        this.screening = screening;
        this.account = account;
        this.seatList = seatList;
        this.price = price;
    }

    private void validateSeats(Screening screening, Set<Seat> seatList) throws InvalidSeatException {
        for (Seat seat : seatList) {
            if (!seat.getRoom().equals(screening.getRoom())) {
                throw new InvalidSeatException("The room of the screening and the seat is not the same");
            }
        }
    }
}
