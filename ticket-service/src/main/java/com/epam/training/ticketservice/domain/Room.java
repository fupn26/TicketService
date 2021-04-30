package com.epam.training.ticketservice.domain;

import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.domain.exception.PriceComponentAlreadyAttachedException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
//@EqualsAndHashCode
public class Room {
    private final String name;
    private final int rows;
    private final int columns;
    private final Set<PriceComponent> priceComponents;

    public Room(String name, int rows, int columns) throws InvalidRowException, InvalidColumnException {
        validateRowNumber(rows);
        validateColumnNumber(columns);

        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.priceComponents = new HashSet<>();
    }

    public Room(String name, int rows, int columns, Set<PriceComponent> priceComponents)
            throws InvalidRowException, InvalidColumnException {
        validateRowNumber(rows);
        validateColumnNumber(columns);

        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.priceComponents = priceComponents;
    }

    public void attachPriceComponent(PriceComponent priceComponentToAttach)
            throws PriceComponentAlreadyAttachedException {
        if (!priceComponents.add(priceComponentToAttach)) {
            throw new PriceComponentAlreadyAttachedException(
                    String.format("Price component %s has already been attached to room %s",
                    priceComponentToAttach.getName(),
                    name)
            );
        }
    }

    private void validateRowNumber(int rowNum) throws InvalidRowException {
        if (rowNum < 1) {
            throw new InvalidRowException("Number of rows is not valid");
        }
    }

    private void validateColumnNumber(int columnNum) throws InvalidColumnException {
        if (columnNum < 1) {
            throw new InvalidColumnException("Number of columns is not valid");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return rows == room.rows && columns == room.columns && name.equals(room.name)
                && priceComponents.equals(room.priceComponents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rows, columns, priceComponents);
    }
}
