package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.RoomMalformedException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.service.RoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @InjectMocks
    private RoomController roomController;
    @Mock
    private RoomService roomService;

    private static final String ROOM_NAME = "best room";
    private static final int ROOM_ROWS = 10;
    private static final int ROOM_COLUMNS = 20;
    private static final Room ROOM = createRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS, Set.of());
    private static final List<Room> ROOMS = List.of(ROOM, ROOM);
    private static final String NO_ROOMS_MESSAGE = "There are no rooms at the moment";
    private static final String ROOM_STRING_TEMPLATE =
            "Room %s with %d seats, %d rows and %d columns";
    private static final String ROOM_LIST_STRING = ROOMS.stream()
            .map(room -> String.format(ROOM_STRING_TEMPLATE, ROOM_NAME, ROOM_ROWS * ROOM_COLUMNS,
                    ROOM_ROWS, ROOM_COLUMNS))
            .collect(Collectors.joining(System.lineSeparator()));
    private static final String ROOM_CREATION_SUCCESS = String.format("Room created: %s", ROOM_NAME);
    private static final String ROOM_UPDATE_SUCCESS = String.format("Room updated: %s", ROOM_NAME);
    private static final String ROOM_DELETE_SUCCESS = String.format("Room deleted: %s", ROOM_NAME);
    private static final String ROOM_ALREADY_EXISTS_MESSAGE = "Room already exists";
    private static final RoomAlreadyExistsException ROOM_ALREADY_EXISTS_EXCEPTION =
            new RoomAlreadyExistsException(ROOM_ALREADY_EXISTS_MESSAGE);
    private static final String ROOM_MALFORMED_MESSAGE = "Room malformed";
    private static final RoomMalformedException ROOM_MALFORMED_EXCEPTION =
            new RoomMalformedException(ROOM_MALFORMED_MESSAGE);
    private static final String ROOM_NOT_FOUND_MESSAGE = "Room not found";
    private static final RoomNotFoundException ROOM_NOT_FOUND_EXCEPTION =
            new RoomNotFoundException(ROOM_NOT_FOUND_MESSAGE);

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
    void testCreateRoomWithNonExistingValidRoom() throws RoomAlreadyExistsException, RoomMalformedException {
        //When
        String actual = roomController.createRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);

        //Then
        verify(roomService, times(1)).createRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);
        assertThat(actual, equalTo(ROOM_CREATION_SUCCESS));
    }

    @Test
    void testCreateRoomWithExistingValidRoomReturnsRoomAlreadyExistsExceptionMessage()
            throws RoomAlreadyExistsException, RoomMalformedException {
        //Given
        doThrow(ROOM_ALREADY_EXISTS_EXCEPTION)
                .when(roomService)
                .createRoom(any(), anyInt(), anyInt());

        //When
        String actual = roomController.createRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);

        //Then
        verify(roomService, times(1)).createRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);
        assertThat(actual, equalTo(ROOM_ALREADY_EXISTS_MESSAGE));
    }

    @Test
    void testCreateRoomWithNonValidRoomReturnsRoomMalformedExceptionMessage()
            throws RoomAlreadyExistsException, RoomMalformedException {
        //Given
        doThrow(ROOM_MALFORMED_EXCEPTION)
                .when(roomService)
                .createRoom(any(), anyInt(), anyInt());

        //When
        String actual = roomController.createRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);

        //Then
        verify(roomService, times(1)).createRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);
        assertThat(actual, equalTo(ROOM_MALFORMED_MESSAGE));
    }

    @Test
    void testListRoomsWithExistingRooms() {
        //Given
        when(roomService.getAllRooms()).thenReturn(ROOMS);

        //When
        String actual = roomController.listRooms();

        //Then
        verify(roomService, times(1)).getAllRooms();
        assertThat(actual, equalTo(ROOM_LIST_STRING));
    }

    @Test
    void testListRoomsWithoutExistingRooms() {
        //Given
        when(roomService.getAllRooms()).thenReturn(List.of());

        //When
        String actual = roomController.listRooms();

        //Then
        verify(roomService, times(1)).getAllRooms();
        assertThat(actual, equalTo(NO_ROOMS_MESSAGE));
    }

    @Test
    void testUpdateRoomWithExistingValidRoomReturnsSuccessString()
            throws RoomNotFoundException, RoomMalformedException {
        //When
        String actual = roomController.updateRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);

        //Then
        verify(roomService, times(1)).updateRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);
        assertThat(actual, equalTo(ROOM_UPDATE_SUCCESS));
    }

    @Test
    void testUpdateRoomWithNonExistingValidRoomReturnsRoomNotFoundExceptionMessage()
            throws RoomNotFoundException, RoomMalformedException {
        doThrow(ROOM_NOT_FOUND_EXCEPTION)
                .when(roomService)
                .updateRoom(any(), anyInt(), anyInt());

        //When
        String actual = roomController.updateRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);

        //Then
        verify(roomService, times(1)).updateRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);
        assertThat(actual, equalTo(ROOM_NOT_FOUND_MESSAGE));
    }

    @Test
    void testUpdateRoomWithNonValidRoomRowsReturnsRoomMalformedExceptionMessage()
            throws RoomNotFoundException, RoomMalformedException {
        doThrow(ROOM_MALFORMED_EXCEPTION)
                .when(roomService)
                .updateRoom(any(), anyInt(), anyInt());

        //When
        String actual = roomController.updateRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);

        //Then
        verify(roomService, times(1)).updateRoom(ROOM_NAME, ROOM_ROWS, ROOM_COLUMNS);
        assertThat(actual, equalTo(ROOM_MALFORMED_MESSAGE));
    }


    @Test
    void testDeleteRoomWithExistingRoomReturnsSuccessString() throws RoomNotFoundException {
        //When
        String actual = roomController.deleteRoom(ROOM_NAME);

        //Then
        verify(roomService, times(1)).deleteRoomByName(ROOM_NAME);
        assertThat(actual, equalTo(ROOM_DELETE_SUCCESS));
    }

    @Test
    void testDeleteRoomWithNonExistingRoomReturnsRoomNotFoundExceptionMessage() throws RoomNotFoundException {
        //Given
        doThrow(ROOM_NOT_FOUND_EXCEPTION)
                .when(roomService)
                .deleteRoomByName(any());

        //When
        String actual = roomController.deleteRoom(ROOM_NAME);

        //Then
        verify(roomService, times(1)).deleteRoomByName(ROOM_NAME);
        assertThat(actual, equalTo(ROOM_NOT_FOUND_MESSAGE));
    }
}
