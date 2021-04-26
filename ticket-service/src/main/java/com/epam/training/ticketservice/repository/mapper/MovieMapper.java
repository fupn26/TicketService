package com.epam.training.ticketservice.repository.mapper;

import com.epam.training.ticketservice.dataccess.entity.MovieEntity;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;

public interface MovieMapper {
    Movie mapToMovie(MovieEntity movieEntityToMap) throws InvalidMovieLengthException;

    MovieEntity mapToMovieEntity(Movie movieToMap);
}
