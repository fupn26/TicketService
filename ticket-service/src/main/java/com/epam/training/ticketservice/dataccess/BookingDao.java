package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.BookingEntity;
import com.epam.training.ticketservice.dataccess.entity.ScreeningEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingDao extends JpaRepository<BookingEntity, UUID> {
    List<BookingEntity> findAllByScreening(ScreeningEntity screeningEntity);

    void deleteAllByScreening_Room_Name(String roomName);

    void deleteAllByScreening_Movie_Title(String movieTitle);
}
