package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingDao extends JpaRepository<BookingEntity, UUID> {
}
