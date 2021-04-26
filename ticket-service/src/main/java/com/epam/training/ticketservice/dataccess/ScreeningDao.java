package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.ScreeningEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ScreeningDao extends JpaRepository<ScreeningEntity, UUID> {
    Optional<ScreeningEntity> findByMovie_TitleAndRoom_NameAndDateTime(
            String movieTitle,
            String roomName,
            LocalDateTime dateTime
    );

    void deleteAllByMovie_Title(String movieTitle);

    void deleteAllByRoom_Name(String roomName);

    void deleteByMovie_TitleAndRoom_NameAndDateTime(
            String movieTitle,
            String roomName,
            LocalDateTime dateTime
    );
}
