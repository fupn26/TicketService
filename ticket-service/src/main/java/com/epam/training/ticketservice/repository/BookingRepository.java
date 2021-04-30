package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.domain.Seat;

import java.awt.print.Book;
import java.util.List;

public interface BookingRepository {
    void createBooking(Booking bookingToCreate);

    List<Booking> getBookingsByUserName(String username);

    List<Booking> getBookingsByScreening(Screening screening);

    void deleteAllByScreening(Screening screening);

    void deleteAllByMovieTitle(String movieTitle);

    void deleteAllByRoomName(String roomName);

    void deleteAllBySeat(Seat seat);
}
