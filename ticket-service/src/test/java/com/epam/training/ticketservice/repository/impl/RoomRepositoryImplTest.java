package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.RoomDao;
import com.epam.training.ticketservice.dataccess.entity.PriceComponentEntity;
import com.epam.training.ticketservice.dataccess.entity.RoomEntity;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.RoomMalformedException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import com.epam.training.ticketservice.repository.mapper.RoomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith({MockitoExtension.class})
class RoomRepositoryImplTest {

    @Spy
    @InjectMocks
    private RoomRepositoryImpl roomRepository;
    @Mock
    private RoomDao roomDao;
    @Mock
    private RoomMapper roomMapper;
    @Mock
    private PriceComponentMapper priceComponentMapper;

    private static final String ROOM_NAME = "best room";
    private static final int ROWS = 10;
    private static final int UPDATE_ROWS = 11;
    private static final int INVALID_ROWS = -10;
    private static final int COLUMNS = 20;
    private static final int UPDATE_COLUMNS = 22;
    private static final String PRICE_COMPONENT_NAME = "glamour";
    private static final int PRICE_COMPONENT_VALUE = -200;
    private static final String UPDATE_PRICE_COMPONENT_NAME = "joy";
    private static final int UPDATE_PRICE_COMPONENT_VALUE = -100;

    private static final PriceComponent PRICE_COMPONENT = new PriceComponent(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final PriceComponent UPDATE_PRICE_COMPONENT = new PriceComponent(UPDATE_PRICE_COMPONENT_NAME,
            UPDATE_PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponent> PRICE_COMPONENTS = Set.of(PRICE_COMPONENT);
    private static final Set<PriceComponent> UPDATE_PRICE_COMPONENTS = Set.of(PRICE_COMPONENT, UPDATE_PRICE_COMPONENT);
    private static final PriceComponentEntity PRICE_COMPONENT_ENTITY = new PriceComponentEntity(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final PriceComponentEntity UPDATE_PRICE_COMPONENT_ENTITY = new PriceComponentEntity(
            UPDATE_PRICE_COMPONENT_NAME,
            UPDATE_PRICE_COMPONENT_VALUE
    );
    private static final Set<PriceComponentEntity> PRICE_COMPONENT_ENTITIES = Set.of(PRICE_COMPONENT_ENTITY);
    private static final Set<PriceComponentEntity> UPDATE_PRICE_COMPONENT_ENTITIES = Set.of(PRICE_COMPONENT_ENTITY,
            UPDATE_PRICE_COMPONENT_ENTITY);


    private Room room;
    private Room updateRoom;
    private List<Room> rooms;
    private RoomEntity roomEntity;
    private RoomEntity updateRoomEntity;
    private List<RoomEntity> roomEntities;
    private RoomEntity invalidRoomEntity;
    private List<RoomEntity> invalidRoomEntities;

    private static Room createRoom(String roomName, int rows, int columns, Set<PriceComponent> priceComponents) {
        Room result = null;
        try {
            result = new Room(roomName, rows, columns, priceComponents);
        } catch (InvalidRowException | InvalidColumnException e) {
            e.printStackTrace();
        }
        return result;
    }

    @BeforeEach
    void init() {
        room = createRoom(ROOM_NAME, ROWS, COLUMNS, PRICE_COMPONENTS);
        updateRoom = createRoom(ROOM_NAME, UPDATE_ROWS, UPDATE_COLUMNS, UPDATE_PRICE_COMPONENTS);
        rooms = List.of(room, room, room);
        roomEntity = new RoomEntity(ROOM_NAME, ROWS, COLUMNS, PRICE_COMPONENT_ENTITIES);
        updateRoomEntity = new RoomEntity(ROOM_NAME, UPDATE_ROWS, UPDATE_COLUMNS, UPDATE_PRICE_COMPONENT_ENTITIES);
        roomEntities = List.of(roomEntity, roomEntity, roomEntity);
        invalidRoomEntity = new RoomEntity(ROOM_NAME, INVALID_ROWS, COLUMNS, PRICE_COMPONENT_ENTITIES);
        invalidRoomEntities = List.of(invalidRoomEntity, invalidRoomEntity, invalidRoomEntity);
    }

    @Test
    void testCreateRoomWithoutError() throws RoomAlreadyExistsException {
        //Given
        when(roomDao.findById(any())).thenReturn(Optional.empty());
        when(roomMapper.mapToRoomEntity(any())).thenReturn(roomEntity);

        //When
        roomRepository.createRoom(room);

        //Then
        verify(roomDao, times(1)).save(roomEntity);
    }

    @Test
    void testCreateRoomWithRoomAlreadyExistsException() {
        //Given
        when(roomDao.findById(any())).thenReturn(Optional.of(roomEntity));

        //Then
        assertThrows(RoomAlreadyExistsException.class, () -> {
            //When
            roomRepository.createRoom(room);
        });
    }

    @Test
    void testGetAllRoomsWithoutError() throws InvalidColumnException, InvalidRowException {
        //Given
        when(roomDao.findAll()).thenReturn(roomEntities);
        when(roomMapper.mapToRoom(any())).thenReturn(room);

        //When
        List<Room> actual = roomRepository.getAllRooms();

        //Then
        assertThat(actual, equalTo(rooms));
    }

    @Test
    void testGetAllRoomWithInvalidColumnException() throws InvalidColumnException, InvalidRowException {
        //Given
        when(roomDao.findAll()).thenReturn(invalidRoomEntities);
        when(roomMapper.mapToRoom(any())).thenThrow(new InvalidColumnException(""));

        //When
        List<Room> actual = roomRepository.getAllRooms();

        //Then
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void testGetAllRoomWithInvalidRowException() throws InvalidColumnException, InvalidRowException {
        //Given
        when(roomDao.findAll()).thenReturn(invalidRoomEntities);
        when(roomMapper.mapToRoom(any())).thenThrow(new InvalidRowException(""));

        //When
        List<Room> actual = roomRepository.getAllRooms();

        //Then
        assertThat(actual, equalTo(List.of()));
    }

    @Test
    void testGetRoomByNameWithoutError() throws RoomNotFoundException, RoomMalformedException,
            InvalidColumnException, InvalidRowException {
        //Given
        when(roomDao.findById(any())).thenReturn(Optional.of(roomEntity));
        when(roomMapper.mapToRoom(any())).thenReturn(room);

        //When
        Room actual = roomRepository.getRoomByName(ROOM_NAME);

        //Then
        assertThat(actual, equalTo(room));
    }

    @Test
    void testGetRoomByNameWithRoomNotFoundException() {
        //Given
        when(roomDao.findById(any())).thenReturn(Optional.empty());

        //Then
        assertThrows(RoomNotFoundException.class, () -> {
            //When
            roomRepository.getRoomByName(ROOM_NAME);
        });
    }

    @Test
    void testGetRoomByNameWithInvalidColumnExceptionThrowsRoomMalformedException() throws InvalidColumnException, InvalidRowException {
        //Given
        when(roomDao.findById(any())).thenReturn(Optional.of(invalidRoomEntity));
        when(roomMapper.mapToRoom(any())).thenThrow(new InvalidColumnException(""));

        //Then
        assertThrows(RoomNotFoundException.class, () -> {
            //When
            roomRepository.getRoomByName(ROOM_NAME);
        });
    }

    @Test
    void testGetRoomByNameWithInvalidRowExceptionThrowsRoomMalformedException() throws InvalidColumnException,
            InvalidRowException {
        //Given
        when(roomDao.findById(any())).thenReturn(Optional.of(invalidRoomEntity));
        when(roomMapper.mapToRoom(any())).thenThrow(new InvalidRowException(""));

        //Then
        assertThrows(RoomNotFoundException.class, () -> {
            //When
            roomRepository.getRoomByName(ROOM_NAME);
        });
    }

    @Test
    void testUpdateRoomWithoutError() throws RoomNotFoundException {
        //Given
        when(roomDao.findById(any())).thenReturn(Optional.of(roomEntity));
        when(priceComponentMapper.mapToPriceComponentEntities(any())).thenReturn(UPDATE_PRICE_COMPONENT_ENTITIES);

        //When
        roomRepository.updateRoom(updateRoom);

        //Then
        verify(roomDao, times(1)).save(updateRoomEntity);
    }


    @Test
    void testUpdateRoomWithRoomNotFoundException() {
        //Given
        when(roomDao.findById(any())).thenReturn(Optional.empty());

        //Then
        assertThrows(RoomNotFoundException.class, () -> {
            //When
            roomRepository.updateRoom(updateRoom);
        });
    }

    @Test
    void testDeleteRoomByNameWithoutError() throws RoomNotFoundException {
        //Given
        when(roomDao.findById(any())).thenReturn(Optional.of(roomEntity));

        //When
        roomRepository.deleteRoomByName(ROOM_NAME);

        //Then
        verify(roomDao, times(1)).deleteById(ROOM_NAME);
    }

    @Test
    void testDeleteRoomByNameWithRoomNotFoundException() {
        //Given
        when(roomDao.findById(any())).thenReturn(Optional.empty());

        //Then
        assertThrows(RoomNotFoundException.class, () -> {
            //When
            roomRepository.deleteRoomByName(ROOM_NAME);
        });
    }
}