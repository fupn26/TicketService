package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.ReservedSeatEntity;
import com.epam.training.ticketservice.dataccess.entity.id.ReservedSeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservedSeatDao extends JpaRepository<ReservedSeatEntity, ReservedSeatId> {
}
