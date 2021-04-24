package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.ScreeningEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScreeningDao extends JpaRepository<ScreeningEntity, UUID> {
}
