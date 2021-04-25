package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dao.entity.RoomEntity;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import com.epam.training.ticketservice.repository.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomMapperImpl implements RoomMapper {

    private final PriceComponentMapper priceComponentMapper;

    @Override
    public Room mapToRoom(RoomEntity roomEntityToMap) throws InvalidRowException, InvalidColumnException {
        return new Room(roomEntityToMap.getName(),
                roomEntityToMap.getRows(),
                roomEntityToMap.getColumns(),
                priceComponentMapper.mapToPriceComponents(roomEntityToMap.getPriceComponentEntities()));
    }

    @Override
    public RoomEntity mapToRoomEntity(Room roomToMap) {
        return new RoomEntity(roomToMap.getName(),
                roomToMap.getRows(),
                roomToMap.getColumns(),
                priceComponentMapper.mapToPriceComponentEntities(roomToMap.getPriceComponents()));
    }
}
