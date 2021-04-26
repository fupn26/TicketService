package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dataccess.entity.MovieEntity;
import com.epam.training.ticketservice.dataccess.entity.PriceComponentEntity;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieMapperImplTest {

    @InjectMocks
    private MovieMapperImpl movieMapper;
    @Mock
    private PriceComponentMapper priceComponentMapper;

    private static final String MOVIE_TITLE = "best movie";
    private static final String MOVIE_GENRE = "drama";
    private static final int INVALID_MOVIE_LENGTH = 0;
    private static final int MOVIE_LENGTH = 100;
    private static final String PRICE_COMPONENT_NAME = "glamour";
    private static final int PRICE_COMPONENT_VALUE = -200;

    private static final PriceComponent PRICE_COMPONENT = new PriceComponent(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponent> PRICE_COMPONENTS = Set.of(PRICE_COMPONENT);
    private static final PriceComponentEntity PRICE_COMPONENT_ENTITY = new PriceComponentEntity(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponentEntity> PRICE_COMPONENT_ENTITIES = Set.of(PRICE_COMPONENT_ENTITY);

    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, PRICE_COMPONENTS);
    private static final MovieEntity MOVIE_ENTITY = new MovieEntity(MOVIE_TITLE, MOVIE_GENRE,
            MOVIE_LENGTH, PRICE_COMPONENT_ENTITIES);

    private static final MovieEntity INVALID_MOVIE_ENTITY = new MovieEntity(MOVIE_TITLE, MOVIE_GENRE,
            INVALID_MOVIE_LENGTH, PRICE_COMPONENT_ENTITIES);

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(MovieMapperImplTest.MOVIE_TITLE, genre, length, priceComponents);
        } catch (InvalidMovieLengthException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Test
    void testMapToMovieWithoutError() throws InvalidMovieLengthException {
        //Given
        when(priceComponentMapper.mapToPriceComponents(any())).thenReturn(PRICE_COMPONENTS);

        //When
        Movie actual = movieMapper.mapToMovie(MOVIE_ENTITY);

        //Then
        assertThat(actual, equalTo(MOVIE));
    }

    @Test
    void testMapToMovieWithInvalidMovieLengthException() {
        //Given
        when(priceComponentMapper.mapToPriceComponents(any())).thenReturn(PRICE_COMPONENTS);

        //Then
        assertThrows(InvalidMovieLengthException.class, () -> {
            //When
            Movie actual = movieMapper.mapToMovie(INVALID_MOVIE_ENTITY);
        });
    }

    @Test
    void mapToMovieEntityWithoutError() {
        //Given
        when(priceComponentMapper.mapToPriceComponentEntities(any())).thenReturn(PRICE_COMPONENT_ENTITIES);

        //When
        MovieEntity actual = movieMapper.mapToMovieEntity(MOVIE);

        //Then
        assertThat(actual, equalTo(MOVIE_ENTITY));
    }
}