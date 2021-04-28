package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dataccess.entity.RoomEntity;
import com.epam.training.ticketservice.dataccess.entity.SeatEntity;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.mapper.RoomMapper;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatMapperImplTest {

    @InjectMocks
    private SeatMapperImpl seatMapper;
    @Mock
    private RoomMapper roomMapper;

    private static final String ROOM_NAME = "best room";
    private static final int ROWS = 10;
    private static final int COLUMNS = 20;
    private static final int SEAT_ROW = 5;
    private static final int INVALID_SEAT_ROW = -5;
    private static final int SEAT_COLUMN = 6;
    private static final int INVALID_SEAT_COLUMN = -6;

    private static final Room ROOM = createRoom(ROOM_NAME, ROWS, COLUMNS, Set.of());
    private static final RoomEntity ROOM_ENTITY = new RoomEntity(ROOM_NAME, ROWS, COLUMNS, Set.of());

    private static final Seat SEAT = createSeat(ROOM, SEAT_ROW, SEAT_COLUMN);
    private static final SeatEntity SEAT_ENTITY = new SeatEntity(ROOM_ENTITY, SEAT_ROW, SEAT_COLUMN);
    private static final SeatEntity INVALID_ROW_SEAT_ENTITY = new SeatEntity(ROOM_ENTITY,
            INVALID_SEAT_ROW, SEAT_COLUMN);
    private static final SeatEntity INVALID_COLUMN_SEAT_ENTITY = new SeatEntity(ROOM_ENTITY,
            SEAT_ROW, INVALID_SEAT_COLUMN);

    private static Seat createSeat(Room room, int row, int column) {
        Seat result = null;
        try {
            result = new Seat(room, row, column);
        } catch (InvalidRowException | InvalidColumnException e) {
            e.printStackTrace();
        }
        return result;
    }

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
    void testMapToSeatWithValidSeatEntityReturnsSeat() throws InvalidColumnException, InvalidRowException {
        //Given
        when(roomMapper.mapToRoom(any())).thenReturn(ROOM);

        //When
        Seat actual = seatMapper.mapToSeat(SEAT_ENTITY);

        //Then
        verify(roomMapper, times(1)).mapToRoom(ROOM_ENTITY);
        assertThat(actual, equalTo(SEAT));
    }

    @Test
    void testMapToSeatWithInvalidRowSeatEntityThrowsInvalidRowException()
            throws InvalidColumnException, InvalidRowException {
        //Given
        when(roomMapper.mapToRoom(any())).thenReturn(ROOM);

        //Then
        assertThrows(InvalidRowException.class, () -> {
            //When
            seatMapper.mapToSeat(INVALID_ROW_SEAT_ENTITY);
        });
    }

    @Test
    void testMapToSeatWithInvalidColumnSeatEntityThrowsInvalidColumnException()
            throws InvalidColumnException, InvalidRowException {
        //Given
        when(roomMapper.mapToRoom(any())).thenReturn(ROOM);

        //Then
        assertThrows(InvalidColumnException.class, () -> {
            //When
            seatMapper.mapToSeat(INVALID_COLUMN_SEAT_ENTITY);
        });
    }

    @Test
    void testMapToSeatWithInvalidRoomRowsSeatEntityThrowsInvalidRowException()
            throws InvalidColumnException, InvalidRowException {
        //Given
        when(roomMapper.mapToRoom(any())).thenThrow(InvalidRowException.class);

        //Then
        assertThrows(InvalidRowException.class, () -> {
            //When
            seatMapper.mapToSeat(SEAT_ENTITY);
        });
    }

    @Test
    void testMapToSeatWithInvalidRoomColumnsSeatEntityThrowsInvalidRowException()
            throws InvalidColumnException, InvalidRowException {
        //Given
        when(roomMapper.mapToRoom(any())).thenThrow(InvalidColumnException.class);

        //Then
        assertThrows(InvalidColumnException.class, () -> {
            //When
            seatMapper.mapToSeat(SEAT_ENTITY);
        });
    }

    @Test
    void testMapToSeatEntityReturnsSeatEntity() {
        //Given
        when(roomMapper.mapToRoomEntity(any())).thenReturn(ROOM_ENTITY);

        //When
        SeatEntity actual = seatMapper.mapToSeatEntity(SEAT);

        //Then
        verify(roomMapper, times(1)).mapToRoomEntity(ROOM);
        assertThat(actual, equalTo(SEAT_ENTITY));
    }
}