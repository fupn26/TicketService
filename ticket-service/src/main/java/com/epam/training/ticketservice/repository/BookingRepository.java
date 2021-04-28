package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.domain.Booking;

import java.util.List;

public interface BookingRepository {
    List<Booking> getBookingsByUserName(String username);
}
