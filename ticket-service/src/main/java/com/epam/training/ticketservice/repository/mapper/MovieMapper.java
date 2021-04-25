package com.epam.training.ticketservice.repository.mapper;

import com.epam.training.ticketservice.dao.entity.MovieEntity;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;

import java.util.List;

public interface MovieMapper {
    Movie mapToMovie(MovieEntity movieEntityToMap) throws InvalidMovieLengthException;

    MovieEntity mapToMovieEntity(Movie movieToMap);
}
