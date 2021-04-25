package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dao.RoomDao;
import com.epam.training.ticketservice.dao.entity.RoomEntity;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.RoomMalformedException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import com.epam.training.ticketservice.repository.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomDao roomDao;
    private final RoomMapper roomMapper;
    private final PriceComponentMapper priceComponentMapper;

    @Override
    public void createRoom(Room roomToCreate) throws RoomAlreadyExistsException {
        if (isRoomExists(roomToCreate.getName())) {
            throw new RoomAlreadyExistsException(String.format("Room already exists: %s",
                    roomToCreate.getName()));
        }
        roomDao.save(roomMapper.mapToRoomEntity(roomToCreate));
    }

    @Override
    public List<Room> getAllRooms() {
        return roomDao.findAll().stream()
                .map(this::mapToRoom)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Room getRoomByName(String roomName) throws RoomNotFoundException, RoomMalformedException {
        Optional<Room> room = mapToRoom(getRoomEntityByName(roomName));
        if (room.isEmpty()) {
            throw new RoomMalformedException(String.format("Room found but malformed: %s", roomName));
        }
        return room.get();
    }

    @Override
    public void updateRoom(Room roomToUpdate) throws RoomNotFoundException {
        RoomEntity roomEntity = getRoomEntityByName(roomToUpdate.getName());
        roomEntity.setRows(roomToUpdate.getRows());
        roomEntity.setColumns(roomToUpdate.getColumns());
        roomEntity.setPriceComponentEntities(priceComponentMapper.mapToPriceComponentEntities(
                roomToUpdate.getPriceComponents())
        );
        roomDao.save(roomEntity);
    }

    @Override
    public void deleteRoomByName(String roomName) throws RoomNotFoundException {
        if (!isRoomExists(roomName)) {
            throw new RoomNotFoundException(String.format("Room not found: %s", roomName));
        }
        roomDao.deleteById(roomName);
    }

    private boolean isRoomExists(String roomName) {
        return roomDao.findById(roomName).isPresent();
    }

    private RoomEntity getRoomEntityByName(String roomName) throws RoomNotFoundException {
        Optional<RoomEntity> roomEntity = roomDao.findById(roomName);
        if (roomEntity.isEmpty()) {
            throw new RoomNotFoundException(String.format("Room not found: %s", roomName));
        }
        return roomEntity.get();
    }

    private Optional<Room> mapToRoom(RoomEntity roomEntityToMap) {
        Optional<Room> result = Optional.empty();
        try {
            result = Optional.of(roomMapper.mapToRoom(roomEntityToMap));
        } catch (InvalidColumnException | InvalidRowException e) {
            log.warn(e.getMessage());
        }
        return result;
    }
}
