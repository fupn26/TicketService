package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingDao extends JpaRepository<BookingEntity, UUID> {
}
