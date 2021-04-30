package com.epam.training.ticketservice.domain;

import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Seat {
    private final UUID id;
    private final Room room;
    private final int rowNum;
    private final int columnNum;

    public Seat(Room room, int rowNum, int columnNum) throws InvalidRowException, InvalidColumnException {
        validateRowNumber(room, rowNum);
        validateColumnNumber(room, columnNum);
        this.room = room;
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.id = null;
    }

    public Seat(UUID id, Room room, int rowNum, int columnNum) throws InvalidRowException, InvalidColumnException {
        validateRowNumber(room, rowNum);
        validateColumnNumber(room, columnNum);
        this.room = room;
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Seat seat = (Seat) o;
        return rowNum == seat.rowNum && columnNum == seat.columnNum && room.equals(seat.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room, rowNum, columnNum);
    }
}
