package com.epam.training.ticketservice.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Screening {
    private final Movie movie;
    private final Room room;
    private final LocalDateTime startDate;
    private final Set<PriceComponent> priceComponentSet;

    public Screening(Movie movie, Room room, LocalDateTime startDate) {
        this.movie = movie;
        this.room = room;
        this.startDate = startDate;
        this.priceComponentSet = new HashSet<>();
    }

    public Screening(Movie movie, Room room, LocalDateTime startDate, Set<PriceComponent> priceComponentSet) {
        this.movie = movie;
        this.room = room;
        this.startDate = startDate;
        this.priceComponentSet = priceComponentSet;
    }
}
