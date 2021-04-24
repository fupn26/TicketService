package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDao extends JpaRepository<MovieEntity, String> {
}
