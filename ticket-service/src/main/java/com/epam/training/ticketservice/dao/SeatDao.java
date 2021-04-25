package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeatDao extends JpaRepository<SeatEntity, UUID> {
}