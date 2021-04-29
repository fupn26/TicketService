package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.exception.SeatAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.SeatNotFoundException;

public interface SeatRepository {
    void createSeat(Seat seatToCreate) throws SeatAlreadyExistsException;

    Seat getSeatByRoomNameRowColumn(String roomName, int row, int column) throws SeatNotFoundException, InvalidColumnException, InvalidRowException;

    void deleteSeatByRoomNameRowColumn(String roomName, int row, int column) throws SeatNotFoundException;

    void deleteAllByRoomName(String roomName);
}
