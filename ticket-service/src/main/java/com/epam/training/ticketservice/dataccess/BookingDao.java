package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.BookingEntity;
import com.epam.training.ticketservice.dataccess.entity.ScreeningEntity;
import com.epam.training.ticketservice.dataccess.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingDao extends JpaRepository<BookingEntity, UUID> {
    List<BookingEntity> findAllByScreening(ScreeningEntity screeningEntity);

    List<BookingEntity> findAllByAccount_Username(String username);

    void deleteAllByScreening_Room_Name(String roomName);

    void deleteAllByScreening_Movie_Title(String movieTitle);

    void deleteAllByScreening(ScreeningEntity screeningEntity);

    void deleteAllBySeatsIsContaining(SeatEntity seatEntity);
}
