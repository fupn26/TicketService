package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.controller.availability.ShellMethodAvailabilityChecker;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.repository.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.RoomMalformedException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final ShellMethodAvailabilityChecker shellMethodAvailabilityChecker;
    private final String roomCreationSuccessTemplate = "Room created: %s";
    private final String roomUpdateSuccessTemplate = "Room updated: %s";
    private final String roomDeleteSuccessTemplate = "Room deleted: %s";
    private final String noRoomsMessage = "There are no rooms at the moment";
    private final String roomStringTemplate = "Room %s with %d seats, %d rows and %d columns";

    public RoomController(RoomService roomService, ShellMethodAvailabilityChecker shellMethodAvailabilityChecker) {
        this.roomService = roomService;
        this.shellMethodAvailabilityChecker = shellMethodAvailabilityChecker;
    }

    @ShellMethod(value = "Creates a room", key = "create room")
    @ShellMethodAvailability("isPrivilegedAccountSignedIn")
    public String createRoom(String roomName, int rows, int columns) {
        String result;
        try {
            roomService.createRoom(roomName, rows, columns);
            result = String.format(roomCreationSuccessTemplate, roomName);
        } catch (RoomAlreadyExistsException | RoomMalformedException e) {
            result = e.getMessage();
        }
        return result;
    }

    @ShellMethod(value = "Lists rooms", key = "list rooms")
    public String listRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return rooms.isEmpty()
                ? noRoomsMessage
                : rooms.stream().map(this::mapRoomToString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @ShellMethod(value = "Updates a room", key = "update room")
    @ShellMethodAvailability("isPrivilegedAccountSignedIn")
    public String updateRoom(String roomName, int rows, int columns) {
        String result;
        try {
            roomService.updateRoom(roomName, rows, columns);
            result = String.format(roomUpdateSuccessTemplate, roomName);
        } catch (RoomNotFoundException | RoomMalformedException e) {
            result = e.getMessage();
        }
        return result;
    }

    @ShellMethod(value = "Deletes a room", key = "delete room")
    @ShellMethodAvailability("isPrivilegedAccountSignedIn")
    public String deleteRoom(String roomName) {
        String result;
        try {
            roomService.deleteRoomByName(roomName);
            result = String.format(roomDeleteSuccessTemplate, roomName);
        } catch (RoomNotFoundException e) {
            result = e.getMessage();
        }
        return result;
    }

    public Availability isPrivilegedAccountSignedIn() {
        return shellMethodAvailabilityChecker.isPrivilegedAccountSignIn();
    }

    private String mapRoomToString(Room room) {
        return String.format(roomStringTemplate,
                room.getName(),
                room.getRows() * room.getColumns(),
                room.getRows(),
                room.getColumns());
    }
}
