package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.ReservedSeatEntity;
import com.epam.training.ticketservice.dao.entity.id.ReservedSeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservedSeatDao extends JpaRepository<ReservedSeatEntity, ReservedSeatId> {
}
