package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.repository.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.RoomMalformedException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;

import java.util.List;

public interface RoomService {
    void createRoom(String name, int rows, int columns) throws RoomAlreadyExistsException, RoomMalformedException;

    List<Room> getAllRooms();

    Room getRoomByName(String name) throws RoomNotFoundException;

    void updateRoom(String name, int rows, int columns) throws RoomNotFoundException, RoomMalformedException;

    void deleteRoomByName(String name) throws RoomNotFoundException;
}
