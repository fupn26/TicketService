package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.PriceComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceComponentDao extends JpaRepository<PriceComponentEntity, String> {
}
