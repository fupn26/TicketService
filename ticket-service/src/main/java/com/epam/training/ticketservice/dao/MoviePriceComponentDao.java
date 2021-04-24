package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.MoviePriceComponentEntity;
import com.epam.training.ticketservice.dao.entity.id.MoviePriceComponentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoviePriceComponentDao extends JpaRepository<MoviePriceComponentEntity, MoviePriceComponentId> {
}
