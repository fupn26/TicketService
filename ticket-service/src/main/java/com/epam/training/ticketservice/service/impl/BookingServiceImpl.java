package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.datatransfer.SeatDto;
import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.domain.Booking;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.domain.exception.InvalidSeatException;
import com.epam.training.ticketservice.repository.BasePriceRepository;
import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.repository.SeatRepository;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.repository.exception.SeatNotFoundException;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.BookingService;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import com.epam.training.ticketservice.service.exception.PrivilegedAccountException;
import com.epam.training.ticketservice.service.exception.SeatAlreadyBookedException;
import com.epam.training.ticketservice.service.exception.SeatDuplicatedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ScreeningRepository screeningRepository;
    private final AccountService accountService;
    private final BasePriceRepository basePriceRepository;
    private final SeatRepository seatRepository;

    @Override
    public Booking createBooking(String movieName, String roomName,
                                 LocalDateTime screeningDateTime,
                                 List<SeatDto> seatDtos)
            throws ScreeningNotFoundException, SeatAlreadyBookedException, SeatDuplicatedException,
            SeatNotFoundException,
            NoSignedInAccountException, PrivilegedAccountException {
        Booking result = null;
        Account account = getAccount();
        Screening screening = getScreening(movieName, roomName, screeningDateTime);
        LinkedHashSet<Seat> seats = mapSeatDtosToSeats(screening.getRoom(), seatDtos);
        checkSeatsToReserved(screening, seats);
        try {
            result = new Booking(screening, account, seats, calculatePrice(screening,
                    basePriceRepository.getBasePrice(), seats.size()));
            bookingRepository.createBooking(result);
        } catch (InvalidSeatException e) {
            log.warn(e.getMessage());
        }
        return result;
    }

    private Account getAccount() throws NoSignedInAccountException, PrivilegedAccountException {
        Account currentAccount = accountService.getSignedInAccount();
        if (currentAccount.isPrivileged())  {
            throw new PrivilegedAccountException(String.format("Privileged account: %s",
                    currentAccount.getUsername()));
        }
        return currentAccount;
    }

    private Screening getScreening(String movieName, String roomName,
                                   LocalDateTime screeningDateTime) throws ScreeningNotFoundException {
        return screeningRepository
                .getScreeningByMovieTitleRoomNameDate(movieName, roomName, screeningDateTime);
    }

    private List<Seat> getReservedSeats(Screening screening) {
        List<Seat> result = new ArrayList<>();
        bookingRepository.getBookingsByScreening(screening).forEach(booking -> result.addAll(booking.getSeatList()));
        return result;
    }

    private void checkSeatsToReserved(Screening screening, LinkedHashSet<Seat> seats)
            throws SeatAlreadyBookedException {
        List<Seat> reservedSeats = getReservedSeats(screening);
        for (Seat seat : seats) {
            for (Seat reservedSeat : reservedSeats) {
                if (reservedSeat.equals(seat)) {
                    throw new SeatAlreadyBookedException(String.format("Seat already booked: %s, %s, %s",
                            screening.getRoom().getName(), seat.getRowNum(), seat.getColumnNum()), seat);
                }
            }
        }
    }

    private LinkedHashSet<Seat> mapSeatDtosToSeats(Room room, List<SeatDto> seatDtos)
            throws SeatNotFoundException, SeatDuplicatedException {
        LinkedHashSet<Seat> result = new LinkedHashSet<>(seatDtos.size());
        for (SeatDto seatDto : seatDtos) {
            try {
                if (!result.add(mapSeatDtoToSeat(room, seatDto))) {
                    throw new SeatDuplicatedException(String.format("Seat duplicated: %s, %s, %s",
                            room.getName(), seatDto.getRowNum(), seatDto.getColumnNum()));
                }
            } catch (InvalidColumnException | InvalidRowException e) {
                throw new SeatNotFoundException(String.format("Seat not found: %s, %s, %s",
                        room.getName(),
                        seatDto.getRowNum(),
                        seatDto.getColumnNum()),
                        room.getName(), seatDto.getRowNum(), seatDto.getColumnNum());
            }
        }
        return result;
    }

    private Seat mapSeatDtoToSeat(Room room, SeatDto seatDtoToMap) throws InvalidColumnException, InvalidRowException,
            SeatNotFoundException {
        return seatRepository.getSeatByRoomNameRowColumn(room.getName(),
                seatDtoToMap.getRowNum(), seatDtoToMap.getColumnNum());
    }

    private int calculatePrice(Screening screening, int basePrice, int numberOfSeats) {
        int result = basePrice;
        result += sumPriceComponents(screening.getPriceComponents());
        result += sumPriceComponents(screening.getMovie().getPriceComponentSet());
        result += sumPriceComponents(screening.getRoom().getPriceComponents());
        return result * numberOfSeats;
    }

    private int sumPriceComponents(Set<PriceComponent> setToSum) {
        return setToSum.stream()
                .map(PriceComponent::getValue)
                .reduce(0, Integer::sum);
    }
}
