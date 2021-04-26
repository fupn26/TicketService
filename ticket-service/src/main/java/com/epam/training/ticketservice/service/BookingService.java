package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.domain.Booking;

import java.util.List;

public interface BookingService {
    List<Booking> getBookingsByUserName(String username);
}
