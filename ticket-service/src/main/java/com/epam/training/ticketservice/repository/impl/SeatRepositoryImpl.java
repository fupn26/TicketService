package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.SeatDao;
import com.epam.training.ticketservice.dataccess.entity.SeatEntity;
import com.epam.training.ticketservice.domain.Seat;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.SeatRepository;
import com.epam.training.ticketservice.repository.exception.SeatAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.SeatNotFoundException;
import com.epam.training.ticketservice.repository.mapper.SeatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatDao seatDao;
    private final SeatMapper seatMapper;

    @Override
    public void createSeat(Seat seatToCreate) throws SeatAlreadyExistsException {
        if (isSeatExists(seatToCreate.getRoom().getName(),
                seatToCreate.getRowNum(),
                seatToCreate.getColumnNum())) {
            throw new SeatAlreadyExistsException(String.format("Seat exists: %s, <%d,%d>",
                    seatToCreate.getRoom().getName(),
                    seatToCreate.getRowNum(),
                    seatToCreate.getColumnNum()));
        }
        seatDao.save(seatMapper.mapToSeatEntity(seatToCreate));
    }

    @Override
    public Seat getSeatByRoomNameRowColumn(String roomName, int row, int column) throws SeatNotFoundException {
        try {
            return seatMapper.mapToSeat(getSeatEntity(roomName, row, column));
        } catch (InvalidColumnException | InvalidRowException e) {
            log.warn(e.getMessage());
            throw new SeatNotFoundException(String.format("Seat not found: %s, <%d,%d>",
                    roomName,
                    row,
                    column), roomName, row, column);
        }
    }

    @Override
    public void deleteSeatByRoomNameRowColumn(String roomName, int row, int column) throws SeatNotFoundException {
        if (!isSeatExists(roomName, row, column)) {
            throw new SeatNotFoundException(String.format("Seat not found: %s, <%d,%d>",
                    roomName,
                    row,
                    column), roomName, row, column);
        }
        seatDao.deleteByRoom_NameAndRowAndColumn(roomName, row, column);
    }

    @Override
    @Transactional
    public void deleteAllByRoomName(String roomName) {
        seatDao.deleteAllByRoom_Name(roomName);
    }

    private boolean isSeatExists(String roomName, int row, int column) {
        return seatDao.findByRoom_NameAndRowAndColumn(roomName, row, column).isPresent();
    }

    private SeatEntity getSeatEntity(String roomName, int row, int column) throws SeatNotFoundException {
        Optional<SeatEntity> seatEntity = seatDao.findByRoom_NameAndRowAndColumn(roomName, row, column);
        if (seatEntity.isEmpty()) {
            throw new SeatNotFoundException(String.format("Seat not found: %s, <%d,%d>",
                    roomName,
                    row,
                    column), roomName, row, column);
        }
        return seatEntity.get();
    }
}
