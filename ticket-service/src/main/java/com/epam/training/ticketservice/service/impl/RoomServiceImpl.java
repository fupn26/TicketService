package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.SeatRepository;
import com.epam.training.ticketservice.repository.exception.*;
import com.epam.training.ticketservice.repository.mapper.RoomMapper;
import com.epam.training.ticketservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;

    @Override
    public void createRoom(String name, int rows, int columns)
            throws RoomAlreadyExistsException, RoomMalformedException {
        try {
            Room room = new Room(name, rows, columns);
            roomRepository.createRoom(room);
            createSeatsOfRoom(room);
        } catch (InvalidRowException | InvalidColumnException e) {
            throw new RoomMalformedException(e.getMessage());
        }
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.getAllRooms();
    }

    @Override
    public Room getRoomByName(String name) throws RoomNotFoundException {
        return roomRepository.getRoomByName(name);
    }

    @Override
    public void updateRoom(String name, int rows, int columns) throws RoomNotFoundException, RoomMalformedException {
        try {
            Room newRoom = new Room(name, rows, columns);
            roomRepository.updateRoom(new Room(name, rows, columns));
            seatRepository.deleteAllByRoomName(name);
            createSeatsOfRoom(newRoom);
        } catch (InvalidRowException | InvalidColumnException e) {
            throw new RoomMalformedException(e.getMessage());
        }
    }

    @Override
    public void deleteRoomByName(String name) throws RoomNotFoundException {
        seatRepository.deleteAllByRoomName(name);
        roomRepository.deleteRoomByName(name);
    }

    private void createSeatsOfRoom(Room room) {
        int rowBound = room.getRows() + 1;
        for (int row = 1; row < rowBound; row++) {
            int columnBound = room.getColumns() + 1;
            for (int column = 1; column < columnBound; column++) {
                createSeat(room, row, column);
            }
        }
    }

    private void createSeat(Room room, int row, int column) {
        try {
            seatRepository.createSeat(new Seat(room, row, column));
        } catch (SeatAlreadyExistsException | InvalidRowException | InvalidColumnException e) {
            log.warn(e.getMessage());
        }
    }
}
