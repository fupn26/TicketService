package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.BookingDao;
import com.epam.training.ticketservice.dataccess.entity.BookingEntity;
import com.epam.training.ticketservice.dataccess.entity.SeatEntity;
import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.domain.exception.InvalidSeatException;
import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.repository.mapper.AccountMapper;
import com.epam.training.ticketservice.repository.mapper.ScreeningMapper;
import com.epam.training.ticketservice.repository.mapper.SeatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingDao bookingDao;
    private final ScreeningMapper screeningMapper;
    private final AccountMapper accountMapper;
    private final SeatMapper seatMapper;

    @Override
    public void createBooking(Booking bookingToCreate) {
        bookingDao.save(mapBookingToBookingEntity(bookingToCreate));
    }

    @Override
    public List<Booking> getBookingsByUserName(String username) {
        return bookingDao.findAllByAccount_Username(username).stream()
                .map(this::mapBookingEntityToBooking)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsByScreening(Screening screening) {
        return bookingDao.findAllByScreening(screeningMapper.mapToScreeningEntity(screening)).stream()
                .map(this::mapBookingEntityToBooking)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAllByScreening(Screening screening) {
        bookingDao.deleteAllByScreening(screeningMapper.mapToScreeningEntity(screening));
    }

    @Override
    @Transactional
    public void deleteAllByMovieTitle(String movieTitle) {
        bookingDao.deleteAllByScreening_Movie_Title(movieTitle);
    }

    @Override
    @Transactional
    public void deleteAllByRoomName(String roomName) {
        bookingDao.deleteAllByScreening_Room_Name(roomName);
    }

    @Override
    @Transactional
    public void deleteAllBySeat(Seat seat) {
        bookingDao.deleteAllBySeatsIsContaining(seatMapper.mapToSeatEntity(seat));
    }

    private BookingEntity mapBookingToBookingEntity(Booking booking) {
        return new BookingEntity(screeningMapper.mapToScreeningEntity(booking.getScreening()),
                accountMapper.mapToAccountEntity(booking.getAccount()),
                mapSeatsToSeatEntities(booking.getSeatList()),
                booking.getPrice());
    }

    private Optional<Booking> mapBookingEntityToBooking(BookingEntity bookingEntity) {
        Optional<Booking> result;
        try {
            result = Optional.of(new Booking(screeningMapper.mapToScreening(bookingEntity.getScreening()),
                    accountMapper.mapToAccount(bookingEntity.getAccount()),
                    mapSeatEntitiesToSeats(bookingEntity.getSeats()),
                    bookingEntity.getPrice()));
        } catch (InvalidMovieLengthException | InvalidColumnException | InvalidRowException | InvalidSeatException e) {
            result = Optional.empty();
        }
        return result;
    }

    private List<SeatEntity> mapSeatsToSeatEntities(LinkedHashSet<Seat> seats) {
        return seats.stream().map(seatMapper::mapToSeatEntity).collect(Collectors.toList());
    }

    private LinkedHashSet<Seat> mapSeatEntitiesToSeats(List<SeatEntity> seatEntities) {
        LinkedHashSet<Seat> result = new LinkedHashSet<>();
        for (SeatEntity seatEntity : seatEntities) {
            try {
                result.add(seatMapper.mapToSeat(seatEntity));
            } catch (InvalidColumnException | InvalidRowException e) {
                log.warn(e.getMessage());
            }
        }
        return result;
    }
}
