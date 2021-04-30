package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dataccess.entity.SeatEntity;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.mapper.RoomMapper;
import com.epam.training.ticketservice.repository.mapper.SeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatMapperImpl implements SeatMapper {

    private final RoomMapper roomMapper;

    @Override
    public Seat mapToSeat(SeatEntity seatEntityToMap) throws InvalidColumnException, InvalidRowException {
        return new Seat(seatEntityToMap.getId(), roomMapper.mapToRoom(seatEntityToMap.getRoom()),
                seatEntityToMap.getRow(), seatEntityToMap.getColumn());
    }

    @Override
    public SeatEntity mapToSeatEntity(Seat seatToMap) {
        if (seatToMap.getId() == null) {
            return new SeatEntity(roomMapper.mapToRoomEntity(seatToMap.getRoom()),
                    seatToMap.getRowNum(), seatToMap.getColumnNum());
        } else {
            return new SeatEntity(seatToMap.getId(), roomMapper.mapToRoomEntity(seatToMap.getRoom()),
                    seatToMap.getRowNum(), seatToMap.getColumnNum());
        }
    }
}
