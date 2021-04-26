package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dataccess.entity.MovieEntity;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.repository.mapper.MovieMapper;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MovieMapperImpl implements MovieMapper {

    private final PriceComponentMapper priceComponentMapper;

    @Override
    public Movie mapToMovie(MovieEntity movieEntityToMap) throws InvalidMovieLengthException {
        return new Movie(movieEntityToMap.getTitle(),
                movieEntityToMap.getGenre(),
                movieEntityToMap.getLength(),
                priceComponentMapper.mapToPriceComponents(movieEntityToMap.getPriceComponents()));
    }

    @Override
    public MovieEntity mapToMovieEntity(Movie movieToMap) {
        return new MovieEntity(movieToMap.getTitle(),
                movieToMap.getGenre(),
                movieToMap.getLength(),
                priceComponentMapper.mapToPriceComponentEntities(movieToMap.getPriceComponentSet()));
    }
}
