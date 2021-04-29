package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.SeatDao;
import com.epam.training.ticketservice.dataccess.entity.RoomEntity;
import com.epam.training.ticketservice.dataccess.entity.SeatEntity;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.exception.SeatAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.SeatNotFoundException;
import com.epam.training.ticketservice.repository.mapper.SeatMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatRepositoryImplTest {

    @InjectMocks
    private SeatRepositoryImpl seatRepository;
    @Mock
    private SeatDao seatDao;
    @Mock
    private SeatMapper seatMapper;

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
    void testCreateSeatWithNonExitingSeat() throws SeatAlreadyExistsException {
        //Given
        when(seatDao.findByRoom_NameAndRowAndColumn(any(), anyInt(), anyInt())).thenReturn(Optional.empty());
        when(seatMapper.mapToSeatEntity(any())).thenReturn(SEAT_ENTITY);

        //When
        seatRepository.createSeat(SEAT);

        //Then
        verify(seatDao, times(1))
                .findByRoom_NameAndRowAndColumn(ROOM_NAME, SEAT_ROW, SEAT_COLUMN);
        verify(seatDao, times(1)).save(SEAT_ENTITY);
    }

    @Test
    void testCreateSeatWithExitingSeatThrowsSeatAlreadyExistsException() {
        //Given
        when(seatDao.findByRoom_NameAndRowAndColumn(any(), anyInt(), anyInt())).thenReturn(Optional.of(SEAT_ENTITY));

        //Then
        assertThrows(SeatAlreadyExistsException.class, () -> {
            //When
            seatRepository.createSeat(SEAT);
        });
    }

    @Test
    void testGetSeatByRoomNameRowColumnWithExistingSeatReturnsSeat() throws SeatNotFoundException,
            InvalidColumnException, InvalidRowException {
        //Given
        when(seatDao.findByRoom_NameAndRowAndColumn(any(), anyInt(), anyInt())).thenReturn(Optional.of(SEAT_ENTITY));
        when(seatMapper.mapToSeat(any())).thenReturn(SEAT);

        //When
        Seat actual = seatRepository.getSeatByRoomNameRowColumn(ROOM_NAME, SEAT_ROW, SEAT_COLUMN);

        //Then
        verify(seatDao, times(1)).findByRoom_NameAndRowAndColumn(any(), anyInt(), anyInt());
        assertThat(actual, equalTo(SEAT));
    }

    @Test
    void testGetSeatByRoomNameRowColumnWithNonExistingSeatThrowsSeatNotFoundException() {
        //Given
        when(seatDao.findByRoom_NameAndRowAndColumn(any(), anyInt(), anyInt())).thenReturn(Optional.empty());

        assertThrows(SeatNotFoundException.class, () -> {
            //When
            seatRepository.getSeatByRoomNameRowColumn(ROOM_NAME, SEAT_ROW, SEAT_COLUMN);
        });
    }

    @Test
    void testGetSeatByRoomNameRowColumnWithExistingSeatAndInvalidColumnExceptionThrowsSeatNotFoundException()
            throws InvalidColumnException, InvalidRowException {
        //Given
        when(seatDao.findByRoom_NameAndRowAndColumn(any(), anyInt(), anyInt())).thenReturn(Optional.of(SEAT_ENTITY));
        when(seatMapper.mapToSeat(any())).thenThrow(InvalidColumnException.class);

        assertThrows(SeatNotFoundException.class, () -> {
            //When
            seatRepository.getSeatByRoomNameRowColumn(ROOM_NAME, SEAT_ROW, SEAT_COLUMN);
        });
    }

    @Test
    void testGetSeatByRoomNameRowColumnWithExistingSeatAndInvalidRowExceptionThrowsSeatNotFoundException()
            throws InvalidColumnException, InvalidRowException {
        //Given
        when(seatDao.findByRoom_NameAndRowAndColumn(any(), anyInt(), anyInt())).thenReturn(Optional.of(SEAT_ENTITY));
        when(seatMapper.mapToSeat(any())).thenThrow(InvalidRowException.class);

        assertThrows(SeatNotFoundException.class, () -> {
            //When
            seatRepository.getSeatByRoomNameRowColumn(ROOM_NAME, SEAT_ROW, SEAT_COLUMN);
        });
    }

    @Test
    void testDeleteAllByRoomNameWithExpectedResult() {
        //When
        seatRepository.deleteAllByRoomName(ROOM_NAME);

        //Then
        verify(seatDao, times(1)).deleteAllByRoom_Name(ROOM_NAME);
    }

    @Test
    void testDeleteSeatByRoomNameRowColumnWithExistingRow() throws SeatNotFoundException {
        //Given
        when(seatDao.findByRoom_NameAndRowAndColumn(any(), anyInt(), anyInt())).thenReturn(Optional.of(SEAT_ENTITY));

        //When
        seatRepository.deleteSeatByRoomNameRowColumn(ROOM_NAME, SEAT_ROW, SEAT_COLUMN);

        //Then
        verify(seatDao, times(1))
                .findByRoom_NameAndRowAndColumn(ROOM_NAME, SEAT_ROW, SEAT_COLUMN);
    }

    @Test
    void testDeleteSeatByRoomNameRowColumnWithNonExistingRowThrowsSeatNotFoundException()
            throws SeatNotFoundException {
        //Given
        when(seatDao.findByRoom_NameAndRowAndColumn(any(), anyInt(), anyInt())).thenReturn(Optional.empty());

        //Then
        assertThrows(SeatNotFoundException.class, () -> {
            //When
            seatRepository.deleteSeatByRoomNameRowColumn(ROOM_NAME, SEAT_ROW, SEAT_COLUMN);
        });
    }
}