package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeatDao extends JpaRepository<SeatEntity, UUID> {
}
