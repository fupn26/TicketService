package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.PriceCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceCategoryDao extends JpaRepository<PriceCategoryEntity, String> {
}
