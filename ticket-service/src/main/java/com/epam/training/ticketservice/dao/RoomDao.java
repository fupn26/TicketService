package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDao extends JpaRepository<RoomEntity, String> {
}
