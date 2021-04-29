package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.SeatRepository;
import com.epam.training.ticketservice.repository.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.RoomMalformedException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.exception.SeatAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @InjectMocks
    private RoomServiceImpl roomService;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private SeatRepository seatRepository;

    private static final String ROOM_NAME = "best room";
    private static final int ROOM_ROWS = 6;
    private static final int INVALID_ROOM_ROWS = -6;
    private static final int ROOM_COLUMNS= 10;
    private static final int INVALID_ROOM_COLUMNS= -10;
    private static final Room ROOM = createRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS, Set.of());
    private static final List<Room> ROOMS = List.of(ROOM, ROOM);

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
    void testCreateRoomWithNonExistingValidRoom() throws RoomMalformedException,
            RoomAlreadyExistsException, SeatAlreadyExistsException {
        //When
        roomService.createRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);

        //Then
        verify(roomRepository, times(1)).createRoom(ROOM);
        verify(seatRepository, times(ROOM_ROWS * ROOM_COLUMNS)).createSeat(any());
    }

    @Test
    void testCreateRoomWithExistingValidRoomThrowsRoomAlreadyExistsException() throws RoomAlreadyExistsException {
        //Given
        doThrow(RoomAlreadyExistsException.class)
                .when(roomRepository)
                .createRoom(any());

        assertThrows(RoomAlreadyExistsException.class, () -> {
            //When
            roomService.createRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);
        });

        //Then
        verify(roomRepository, times(1)).createRoom(ROOM);
    }

    @Test
    void testCreateRoomWithNonValidRowsRoomThrowsRoomMalformedException() {
        //Then
        assertThrows(RoomMalformedException.class, () -> {
            //When
            roomService.createRoom(ROOM_NAME, INVALID_ROOM_ROWS, ROOM_COLUMNS);
        });
    }

    @Test
    void testCreateRoomWithNonValidColumnsRoomThrowsRoomMalformedException() {
        //Then
        assertThrows(RoomMalformedException.class, () -> {
            //When
            roomService.createRoom(ROOM_NAME, ROOM_ROWS, INVALID_ROOM_COLUMNS);
        });
    }

    @Test
    void testGetAllRoomsWithExistingRoomsReturnsListOfRooms() {
        //Given
        when(roomRepository.getAllRooms()).thenReturn(ROOMS);

        //When
        List<Room> actual = roomService.getAllRooms();

        //Then
        verify(roomRepository, times(1)).getAllRooms();
        assertThat(actual, equalTo(ROOMS));
    }

    @Test
    void testGetAllRoomsWithoutExistingRoomsReturnsEmptyList() {
        //Given
        when(roomRepository.getAllRooms()).thenReturn(List.of());

        //When
        List<Room> actual = roomService.getAllRooms();

        //Then
        verify(roomRepository, times(1)).getAllRooms();
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void testGetRoomByNameWithExistingRoomReturnsRoom() throws RoomNotFoundException {
        //Given
        when(roomRepository.getRoomByName(any())).thenReturn(ROOM);

        //When
        Room actual = roomService.getRoomByName(ROOM_NAME);

        //Then
        verify(roomRepository, times(1)).getRoomByName(ROOM_NAME);
        assertThat(actual, equalTo(ROOM));
    }

    @Test
    void testGetRoomByNameWithNonExistingRoomThrowsRoomNotFoundException() throws RoomNotFoundException {
        //Given
        when(roomRepository.getRoomByName(any())).thenThrow(RoomNotFoundException.class);

        //Then
        assertThrows(RoomNotFoundException.class, () -> {
            //When
            roomService.getRoomByName(ROOM_NAME);
        });
    }

    @Test
    void testUpdateRoomWithExistingRoom() throws RoomNotFoundException,
            RoomMalformedException, SeatAlreadyExistsException {
        //When
        roomService.updateRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);

        //Then
        verify(roomRepository, times(1)).updateRoom(ROOM);
        verify(seatRepository, times(1)).deleteAllByRoomName(ROOM_NAME);
        verify(seatRepository, times(ROOM_ROWS * ROOM_COLUMNS))
                .createSeat(any());
    }

    @Test
    void testUpdateRoomWithNonExistingRoomThrowsRoomNotFoundException() throws RoomNotFoundException {
        //Given
        doThrow(RoomNotFoundException.class)
                .when(roomRepository)
                .updateRoom(any());

        //Then
        assertThrows(RoomNotFoundException.class, () -> {
            //When
            roomService.updateRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);
        });
    }

    @Test
    void testUpdateRoomWithInvalidRowsRoomThrowsRoomMalformedException() {
        //Then
        assertThrows(RoomMalformedException.class, () -> {
            //When
            roomService.updateRoom(ROOM_NAME, INVALID_ROOM_ROWS, ROOM_COLUMNS);
        });
    }

    @Test
    void testUpdateRoomWithInvalidColumnsRoomThrowsRoomMalformedException() {
        //Then
        assertThrows(RoomMalformedException.class, () -> {
            //When
            roomService.updateRoom(ROOM_NAME, ROOM_ROWS, INVALID_ROOM_COLUMNS);
        });
    }

    @Test
    void testDeleteRoomByNameWithExistingRoom() throws RoomNotFoundException {
        //When
        roomService.deleteRoomByName(ROOM_NAME);

        //Then
        verify(seatRepository, times(1)).deleteAllByRoomName(ROOM_NAME);
        verify(roomRepository, times(1)).deleteRoomByName(ROOM_NAME);
    }

    @Test
    void testDeleteRoomByNameWithNonExistingRoomThrowsRoomNotFoundException() throws RoomNotFoundException {
        //Given
        doThrow(RoomNotFoundException.class)
            .when(roomRepository)
            .deleteRoomByName(any());

        assertThrows(RoomNotFoundException.class, () -> {
            //When
            roomService.deleteRoomByName(ROOM_NAME);
        });

        //Then
        verify(seatRepository, times(1)).deleteAllByRoomName(ROOM_NAME);
    }
}