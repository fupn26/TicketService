package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.datatransfer.SeatDto;
import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.exception.SeatNotFoundException;
import com.epam.training.ticketservice.service.BookingService;
import com.epam.training.ticketservice.service.exception.SeatAlreadyBookedException;
import com.epam.training.ticketservice.service.exception.SeatDuplicatedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Override
    public Booking createBooking(String movieName, String roomName,
                                 LocalDateTime screeningDateTime,
                                 List<SeatDto> seats)
            throws MovieNotFoundException, RoomNotFoundException,
            SeatAlreadyBookedException, SeatDuplicatedException, SeatNotFoundException {
        return null;
    }

    @Override
    public List<Booking> getBookingsByUserName(String username) {
        return List.of();
    }
}
