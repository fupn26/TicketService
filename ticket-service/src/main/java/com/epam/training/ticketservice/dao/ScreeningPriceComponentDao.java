package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.ScreeningPriceComponentEntity;
import com.epam.training.ticketservice.dao.entity.id.ScreeningPriceComponentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningPriceComponentDao
        extends JpaRepository<ScreeningPriceComponentEntity, ScreeningPriceComponentId> {
}
