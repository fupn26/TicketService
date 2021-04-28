package com.epam.training.ticketservice.repository.mapper;

import com.epam.training.ticketservice.dao.entity.SeatEntity;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;

public interface SeatMapper {
    Seat mapToSeat(SeatEntity seatEntityToMap) throws InvalidColumnException, InvalidRowException;

    SeatEntity mapToSeatEntity(Seat seatToMap);
}
