package com.epam.training.ticketservice.repository.mapper;

import com.epam.training.ticketservice.dao.entity.RoomEntity;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;

public interface RoomMapper {
    Room mapToRoom(RoomEntity roomEntityToMap) throws InvalidRowException, InvalidColumnException;

    RoomEntity mapToRoomEntity(Room roomToMap);
}
