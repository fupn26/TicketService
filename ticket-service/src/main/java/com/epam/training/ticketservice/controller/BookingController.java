package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.controller.availability.ShellMethodAvailabilityChecker;
import com.epam.training.ticketservice.controller.exception.SeatListInvalidException;
import com.epam.training.ticketservice.controller.mapper.DateTimeMapper;
import com.epam.training.ticketservice.datatransfer.SeatDto;
import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.repository.exception.SeatNotFoundException;
import com.epam.training.ticketservice.service.BookingService;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import com.epam.training.ticketservice.service.exception.PrivilegedAccountException;
import com.epam.training.ticketservice.service.exception.SeatAlreadyBookedException;
import com.epam.training.ticketservice.service.exception.SeatDuplicatedException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
public class BookingController {

    private final BookingService bookingService;
    private final ShellMethodAvailabilityChecker shellMethodAvailabilityChecker;
    private final DateTimeMapper dateTimeMapper;
    private final String seatListParseError
            = "Seat list can't be parsed. Use this format: '<row>,<column> <row>,<column>'";
    private final String seatNotFoundMessageTemplate = "Seat %s does not exist in this room";
    private final String seatAlreadyTakenMessageTemplate = "Seat %s is already taken";
    private final String bookingSuccessMessageTemplate = "Seats booked: %s; the price for this booking is %d HUF";
    private final String seatStringTemplate = "(%d,%d)";
    private final String dateTimeFormat = "yyyy-MM-dd HH:mm";

    public BookingController(BookingService bookingService,
                             ShellMethodAvailabilityChecker shellMethodAvailabilityChecker,
                             DateTimeMapper dateTimeMapper) {
        this.bookingService = bookingService;
        this.shellMethodAvailabilityChecker = shellMethodAvailabilityChecker;
        this.dateTimeMapper = dateTimeMapper;
    }

    @ShellMethod(value = "Books seats for screening", key = "book")
    @ShellMethodAvailability("isNonPrivilegedAccountSignedIn")
    public String book(String movieTitle, String roomName, String screeningDateTime, String seatsToBook) {
        String result;
        try {
            Booking booking = bookingService.createBooking(movieTitle,
                    roomName,
                    dateTimeMapper.mapToLocalDateTime(screeningDateTime, dateTimeFormat),
                    mapStringToSeatDtoList(seatsToBook));
            result = mapBookingToString(booking);
        } catch (SeatDuplicatedException | ScreeningNotFoundException
                | NoSignedInAccountException | PrivilegedAccountException e) {
            result = e.getMessage();
        } catch (SeatNotFoundException e) {
            result = String.format(seatNotFoundMessageTemplate,
                    String.format(seatStringTemplate,e.getRow(), e.getColumn()));
        } catch (SeatAlreadyBookedException e) {
            result = String.format(seatAlreadyTakenMessageTemplate,
                    String.format(seatStringTemplate, e.getSeat().getRowNum(), e.getSeat().getColumnNum()));
        } catch (SeatListInvalidException e) {
            result = seatListParseError;
        }
        return result;
    }

    public Availability isNonPrivilegedAccountSignedIn() {
        return shellMethodAvailabilityChecker.isNonPrivilegedAccountSignIn();
    }

    private List<SeatDto> mapStringToSeatDtoList(String seats) throws SeatListInvalidException {
        String[] parts = seats.trim().split("\\s");
        List<SeatDto> result = Arrays.stream(parts)
                .map(this::mapStringToSeatDto)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (result.isEmpty() || parts.length != result.size()) {
            throw new SeatListInvalidException("");
        }
        return result;
    }

    private Optional<SeatDto> mapStringToSeatDto(String rowColumnPair) {
        String[] pair = rowColumnPair.split(",");
        Optional<SeatDto> result;
        if (pair.length != 2) {
            result = Optional.empty();
        } else {
            try {
                result = Optional.of(new SeatDto(Integer.parseInt(pair[0]),
                        Integer.parseInt(pair[1])));
            } catch (NumberFormatException e) {
                result = Optional.empty();
            }
        }
        return result;
    }

    private String mapBookingToString(Booking bookingToMap) {
        return String.format(bookingSuccessMessageTemplate, bookingToMap.getSeatList().stream()
                .map(this::mapSeatToString)
                .collect(Collectors.joining(", ")), bookingToMap.getPrice());
    }

    private String mapSeatToString(Seat seatToMap) {
        return String.format(seatStringTemplate, seatToMap.getRowNum(), seatToMap.getColumnNum());
    }
}
