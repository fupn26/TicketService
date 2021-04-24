package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.PriceComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceComponentDao extends JpaRepository<PriceComponentEntity, String> {
}
