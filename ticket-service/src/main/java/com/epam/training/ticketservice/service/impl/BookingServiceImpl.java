package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Override
    public List<Booking> getBookingsByUserName(String username) {
        return List.of();
    }
}
