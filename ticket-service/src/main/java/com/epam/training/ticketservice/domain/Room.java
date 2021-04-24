package com.epam.training.ticketservice.domain;

import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class Room {
    private final String name;
    private final int rows;
    private final int columns;
    private final Set<PriceComponent> priceComponentSet;

    public Room(String name, int rows, int columns) throws InvalidRowException, InvalidColumnException {
        validateRowNumber(rows);
        validateColumnNumber(columns);

        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.priceComponentSet = new HashSet<>();
    }

    public Room(String name, int rows, int columns, Set<PriceComponent> priceComponentSet)
            throws InvalidRowException, InvalidColumnException {
        validateRowNumber(rows);
        validateColumnNumber(columns);

        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.priceComponentSet = priceComponentSet;
    }

    private void validateRowNumber(int rowNum) throws InvalidRowException {
        if (rowNum < 0) {
            throw new InvalidRowException("Number of rows is not valid");
        }
    }

    private void validateColumnNumber(int columnNum) throws InvalidColumnException {
        if (columnNum < 0) {
            throw new InvalidColumnException("Number of columns is not valid");
        }
    }
}
