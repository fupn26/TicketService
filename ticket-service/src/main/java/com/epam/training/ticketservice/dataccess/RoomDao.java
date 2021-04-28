package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDao extends JpaRepository<RoomEntity, String> {
}
