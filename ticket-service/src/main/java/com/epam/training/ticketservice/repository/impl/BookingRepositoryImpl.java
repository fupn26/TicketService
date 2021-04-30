package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.repository.BookingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    @Override
    public void createBooking(Booking bookingToCreate) {

    }

    @Override
    public List<Booking> getBookingsByUserName(String username) {
        return null;
    }

    @Override
    public List<Booking> getBookingsByScreening(Screening screening) {
        return null;
    }
}
