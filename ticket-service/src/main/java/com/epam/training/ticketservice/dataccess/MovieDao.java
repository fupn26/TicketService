package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDao extends JpaRepository<MovieEntity, String> {
}
