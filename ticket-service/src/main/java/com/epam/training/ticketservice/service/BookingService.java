package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.datatransfer.SeatDto;
import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.repository.exception.SeatNotFoundException;
import com.epam.training.ticketservice.service.exception.SeatAlreadyBookedException;
import com.epam.training.ticketservice.service.exception.SeatDuplicatedException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    Booking createBooking(String movieName, String roomName, LocalDateTime screeningDateTime, List<SeatDto> seats)
            throws MovieNotFoundException, RoomNotFoundException, SeatNotFoundException,
            SeatAlreadyBookedException, SeatDuplicatedException, ScreeningNotFoundException;

    List<Booking> getBookingsByUserName(String username);
}
