package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.repository.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.RoomMalformedException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Override
    public void createRoom(String name, int rows, int columns)
            throws RoomAlreadyExistsException, RoomMalformedException {

    }

    @Override
    public List<Room> getAllRooms() {
        return null;
    }

    @Override
    public Room getRoomByName(String name) throws RoomNotFoundException {
        return null;
    }

    @Override
    public void updateRoom(String name, int rows, int columns) throws RoomNotFoundException, RoomMalformedException {

    }

    @Override
    public void deleteRoomByName(String name) throws RoomNotFoundException {

    }
}
