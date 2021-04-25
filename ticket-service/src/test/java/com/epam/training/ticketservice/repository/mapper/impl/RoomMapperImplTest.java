package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dao.entity.MovieEntity;
import com.epam.training.ticketservice.dao.entity.PriceComponentEntity;
import com.epam.training.ticketservice.dao.entity.RoomEntity;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomMapperImplTest {

    @InjectMocks
    private RoomMapperImpl roomMapper;
    @Mock
    private PriceComponentMapper priceComponentMapper;

    private static final String ROOM_NAME = "best room";
    private static final int ROWS = 10;
    private static final int INVALID_ROWS = -10;
    private static final int COLUMNS = 20;
    private static final int INVALID_COLUMNS = -10;
    private static final String PRICE_COMPONENT_NAME = "glamour";
    private static final int PRICE_COMPONENT_VALUE = -200;

    private static final PriceComponent PRICE_COMPONENT = new PriceComponent(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponent> PRICE_COMPONENTS = Set.of(PRICE_COMPONENT);
    private static final PriceComponentEntity PRICE_COMPONENT_ENTITY = new PriceComponentEntity(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponentEntity> PRICE_COMPONENT_ENTITIES = Set.of(PRICE_COMPONENT_ENTITY);

    private static final Room ROOM = createRoom(ROOM_NAME, ROWS, COLUMNS, PRICE_COMPONENTS);
    private static final RoomEntity ROOM_ENTITY = new RoomEntity(ROOM_NAME, ROWS, COLUMNS, PRICE_COMPONENT_ENTITIES);
    private static final RoomEntity INVALID_ROWS_ROOM_ENTITY = new RoomEntity(ROOM_NAME, INVALID_ROWS,
            COLUMNS, PRICE_COMPONENT_ENTITIES);
    private static final RoomEntity INVALID_COLUMNS_ROOM_ENTITY = new RoomEntity(ROOM_NAME, ROWS,
            INVALID_COLUMNS, PRICE_COMPONENT_ENTITIES);

    private static Room createRoom(String roomName, int rows, int columns, Set<PriceComponent> priceComponents) {
        Room result = null;
        try {
            result = new Room(roomName, rows, columns, priceComponents);
        } catch (InvalidRowException | InvalidColumnException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Test
    void testMapToRoomWithoutError() throws InvalidColumnException, InvalidRowException {
        //Given
        when(priceComponentMapper.mapToPriceComponents(any())).thenReturn(PRICE_COMPONENTS);

        //When
        Room actual = roomMapper.mapToRoom(ROOM_ENTITY);

        //Then
        assertThat(actual, equalTo(ROOM));
    }

    @Test
    void testMapToRoomWithInvalidColumnException() {
        //Given
        when(priceComponentMapper.mapToPriceComponents(any())).thenReturn(PRICE_COMPONENTS);

        //Then
        assertThrows(InvalidColumnException.class, () -> {
            //When
            roomMapper.mapToRoom(INVALID_COLUMNS_ROOM_ENTITY);
        });
    }

    @Test
    void testMapToRoomWithInvalidRowException() {
        //Given
        when(priceComponentMapper.mapToPriceComponents(any())).thenReturn(PRICE_COMPONENTS);

        //Then
        assertThrows(InvalidRowException.class, () -> {
            //When
            roomMapper.mapToRoom(INVALID_ROWS_ROOM_ENTITY);
        });
    }

    @Test
    void testMapToRoomEntityWithoutError() {
        //Given
        when(priceComponentMapper.mapToPriceComponentEntities(any())).thenReturn(PRICE_COMPONENT_ENTITIES);

        //When
        RoomEntity actual = roomMapper.mapToRoomEntity(ROOM);

        //Then
        assertThat(actual, equalTo(ROOM_ENTITY));
    }
}