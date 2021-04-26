package com.epam.training.ticketservice.domain;

import com.epam.training.ticketservice.domain.exception.PriceComponentAlreadyAttachedException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class Screening {
    private final Movie movie;
    private final Room room;
    private final LocalDateTime startDate;
    private final Set<PriceComponent> priceComponents;

    public Screening(Movie movie, Room room, LocalDateTime startDate) {
        this.movie = movie;
        this.room = room;
        this.startDate = startDate;
        this.priceComponents = new HashSet<>();
    }

    public Screening(Movie movie, Room room, LocalDateTime startDate, Set<PriceComponent> priceComponents) {
        this.movie = movie;
        this.room = room;
        this.startDate = startDate;
        this.priceComponents = priceComponents;
    }

    public void attachPriceComponent(PriceComponent priceComponentToAttach)
            throws PriceComponentAlreadyAttachedException {
        if (!priceComponents.add(priceComponentToAttach)) {
            throw new PriceComponentAlreadyAttachedException(
                    String.format("Price component %s has already been attached to screening %s",
                            priceComponentToAttach.getName(),
                            this
                            )
            );
        }
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("<%s> <%s> <%s>", movie.getTitle(), room.getName(), startDate.format(formatter));
    }
}
