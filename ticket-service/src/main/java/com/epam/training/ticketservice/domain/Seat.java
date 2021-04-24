package com.epam.training.ticketservice.domain;

import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Seat {
    private final Room room;
    private final int rowNum;
    private final int columnNum;

    public Seat(Room room, int rowNum, int columnNum) throws InvalidRowException, InvalidColumnException {
        validateRowNumber(room, rowNum);
        validateColumnNumber(room, columnNum);
        this.room = room;
        this.rowNum = rowNum;
        this.columnNum = columnNum;
    }

    private void validateRowNumber(Room room, int rowNum) throws InvalidRowException {
        if (rowNum < 0  || room.getRows() < rowNum) {
            throw new InvalidRowException("Row number is not valid");
        }
    }

    private void validateColumnNumber(Room room, int columnNum) throws InvalidColumnException {
        if (columnNum < 0 || room.getColumns() < columnNum) {
            throw new InvalidColumnException("Column number is not valid");
        }
    }
}
