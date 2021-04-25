package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.repository.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.RoomMalformedException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;

import java.util.List;

public interface RoomRepository {
    void createRoom(Room roomToCreate) throws RoomAlreadyExistsException;

    List<Room> getAllRooms();

    Room getRoomByName(String roomName) throws RoomNotFoundException, RoomMalformedException;

    void updateRoom(Room roomToUpdate) throws RoomNotFoundException;

    void deleteRoomByName(String roomName) throws RoomNotFoundException;
}
