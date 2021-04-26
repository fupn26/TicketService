package com.epam.training.ticketservice.domain;

import com.epam.training.ticketservice.domain.exception.InvalidSeatException;
import lombok.Getter;

import java.util.Set;

@Getter
public class Booking {
    private final Screening screening;
    private final Account account;
    private final Set<Seat> seatList;
    private final int price;

    public Booking(Screening screening, Account account, Set<Seat> seatList, int basePrice)
            throws InvalidSeatException {
        validateSeats(screening, seatList);

        this.screening = screening;
        this.account = account;
        this.seatList = seatList;
        this.price = calculatePrice(basePrice);
    }

    private void validateSeats(Screening screening, Set<Seat> seatList) throws InvalidSeatException {
        for (Seat seat : seatList) {
            if (!seat.getRoom().equals(screening.getRoom())) {
                throw new InvalidSeatException("The room of the screening and the seat is not the same");
            }
        }
    }

    private int calculatePrice(int basePrice) {
        int result = basePrice;
        result += sumPriceComponents(screening.getPriceComponents());
        result += sumPriceComponents(screening.getMovie().getPriceComponentSet());
        result += sumPriceComponents(screening.getRoom().getPriceComponents());
        return result * seatList.size();
    }

    private int sumPriceComponents(Set<PriceComponent> setToSum) {
        return setToSum.stream()
                .map(PriceComponent::getValue)
                .reduce(0, Integer::sum);
    }
}
