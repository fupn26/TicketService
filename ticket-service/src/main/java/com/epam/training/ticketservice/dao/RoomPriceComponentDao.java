package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.RoomPriceComponentEntity;
import com.epam.training.ticketservice.dao.entity.id.RoomPriceComponentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomPriceComponentDao extends JpaRepository<RoomPriceComponentEntity, RoomPriceComponentId> {
}
