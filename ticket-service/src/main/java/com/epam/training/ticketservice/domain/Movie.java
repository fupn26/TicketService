package com.epam.training.ticketservice.domain;

import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.PriceComponentAlreadyAttachedException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class Movie {
    private final String title;
    private final String genre;
    private final int length;
    private final Set<PriceComponent> priceComponentSet;

    public Movie(String title, String genre, int length) throws InvalidMovieLengthException {
        validateMovieLength(length);
        this.title = title;
        this.genre = genre;
        this.length = length;
        this.priceComponentSet = new HashSet<>();
    }

    public Movie(String title, String genre, int length, Set<PriceComponent> priceComponentSet)
            throws InvalidMovieLengthException {
        validateMovieLength(length);
        this.title = title;
        this.genre = genre;
        this.length = length;
        this.priceComponentSet = priceComponentSet;
    }

    public void attachPriceComponent(PriceComponent priceComponent) throws PriceComponentAlreadyAttachedException {
        if (!priceComponentSet.add(priceComponent)) {
            throw new PriceComponentAlreadyAttachedException(
                    String.format("Price component %s has already been attached to movie %s",
                            priceComponent.getName(),
                            this.title)
            );
        }
    }

    private void validateMovieLength(int length) throws InvalidMovieLengthException {
        if (length < 1) {
            throw new InvalidMovieLengthException("The movie length can't be smaller than one minute");
        }
    }
}
