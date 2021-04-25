package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dao.MovieDao;
import com.epam.training.ticketservice.dao.entity.MovieEntity;
import com.epam.training.ticketservice.dao.entity.PriceComponentEntity;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class MovieRepositoryImplTest {

    @Spy
    @InjectMocks
    private MovieRepositoryImpl movieRepository;
    @Mock
    private MovieDao movieDao;

    private static final String MOVIE_TITLE = "best movie";
    private static final String MOVIE_GENRE = "drama";
    private static final String UPDATE_MOVIE_GENRE = "comedy";
    private static final int INVALID_MOVIE_LENGTH = 0;
    private static final int MOVIE_LENGTH = 100;
    private static final int UPDATE_MOVIE_LENGTH = 120;
    private static final String PRICE_COMPONENT_NAME = "glamour";
    private static final int PRICE_COMPONENT_VALUE = -200;
    private static final String UPDATE_PRICE_COMPONENT_NAME = "joy";
    private static final int UPDATE_PRICE_COMPONENT_VALUE = -100;


    private static final PriceComponent PRICE_COMPONENT = new PriceComponent(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final PriceComponent UPDATE_PRICE_COMPONENT = new PriceComponent(UPDATE_PRICE_COMPONENT_NAME,
            UPDATE_PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponent> PRICE_COMPONENTS = Set.of(PRICE_COMPONENT);
    private static final Set<PriceComponent> UPDATE_PRICE_COMPONENTS = Set.of(PRICE_COMPONENT, UPDATE_PRICE_COMPONENT);
    private static final PriceComponentEntity PRICE_COMPONENT_ENTITY = new PriceComponentEntity(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final PriceComponentEntity UPDATE_PRICE_COMPONENT_ENTITY = new PriceComponentEntity(
            UPDATE_PRICE_COMPONENT_NAME,
            UPDATE_PRICE_COMPONENT_VALUE
    );
    private static final Set<PriceComponentEntity> PRICE_COMPONENT_ENTITIES = Set.of(PRICE_COMPONENT_ENTITY);
    private static final Set<PriceComponentEntity> UPDATE_PRICE_COMPONENT_ENTITIES = Set.of(PRICE_COMPONENT_ENTITY,
            UPDATE_PRICE_COMPONENT_ENTITY);

    private Movie MOVIE;
    private Movie UPDATE_MOVIE;
    private List<Movie> MOVIES;

    private MovieEntity MOVIE_ENTITY;
    private MovieEntity UPDATE_MOVIE_ENTITY;
    private MovieEntity MOVIE_ENTITY_INVALID;
    private List<MovieEntity> MOVIE_ENTITIES;
    private List<MovieEntity> MOVIE_ENTITIES_INVALID;

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(MovieRepositoryImplTest.MOVIE_TITLE, genre, length, priceComponents);
        } catch (InvalidMovieLengthException e) {
            e.printStackTrace();
        }
        return result;
    }

    @BeforeEach
    void init() {
        MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, PRICE_COMPONENTS);
        UPDATE_MOVIE = createMovie(UPDATE_MOVIE_GENRE,
                UPDATE_MOVIE_LENGTH, UPDATE_PRICE_COMPONENTS);
        MOVIES = List.of(MOVIE, MOVIE, MOVIE);
        MOVIE_ENTITY = new MovieEntity(MOVIE_TITLE, MOVIE_GENRE,
                MOVIE_LENGTH, PRICE_COMPONENT_ENTITIES);
       UPDATE_MOVIE_ENTITY = new MovieEntity(MOVIE_TITLE, UPDATE_MOVIE_GENRE,
                UPDATE_MOVIE_LENGTH, UPDATE_PRICE_COMPONENT_ENTITIES);
        MOVIE_ENTITY_INVALID = new MovieEntity(MOVIE_TITLE, MOVIE_GENRE,
                INVALID_MOVIE_LENGTH, PRICE_COMPONENT_ENTITIES);
        MOVIE_ENTITIES = List.of(MOVIE_ENTITY, MOVIE_ENTITY, MOVIE_ENTITY);
        MOVIE_ENTITIES_INVALID = List.of(MOVIE_ENTITY_INVALID,
                MOVIE_ENTITY_INVALID, MOVIE_ENTITY_INVALID);
    }

    @Test
    void testCreateMovieWithoutError() throws MovieAlreadyExistsException {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.empty());

        //When
        movieRepository.createMovie(MOVIE);

        //Then
        ArgumentCaptor<MovieEntity> movieEntityArgumentCaptor = ArgumentCaptor.forClass(MovieEntity.class);
        verify(movieDao, times(1)).save(movieEntityArgumentCaptor.capture());
        MovieEntity actual = movieEntityArgumentCaptor.getValue();
        assertThat(actual, equalTo(MOVIE_ENTITY));
    }

    @Test
    void testCreateMovieWithMovieAlreadyExitsException() {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.of(MOVIE_ENTITY));

        //Then
        assertThrows(MovieAlreadyExistsException.class, () -> {
            //When
            movieRepository.createMovie(MOVIE);
        });
    }

    @Test
    void testGetAllMoviesWithoutError() {
        //Given
        when(movieDao.findAll()).thenReturn(MOVIE_ENTITIES);

        //When
        List<Movie> actual = movieRepository.getAllMovies();

        //Then
        assertThat(MOVIES.equals(actual), equalTo(true));
        //assertThat(actual, equalTo(MOVIES));
    }

    @Test
    void testGetAllMoviesWithMappingProblems() {
        //Given
        when(movieDao.findAll()).thenReturn(MOVIE_ENTITIES_INVALID);

        //When
        List<Movie> actual = movieRepository.getAllMovies();

        //Then
        assertThat(actual.isEmpty(), equalTo(true));
    }

    @Test
    void testGetMovieByTitleWithoutError() throws MovieMalformedException, MovieNotFoundException {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.of(MOVIE_ENTITY));

        //When
        Movie actual = movieRepository.getMovieByTitle(MOVIE_TITLE);

        //Then
        assertThat(actual, equalTo(MOVIE));
    }

    @Test
    void testGetMovieByTitleWithMovieNotFoundException() {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.empty());

        //Then
        assertThrows(MovieNotFoundException.class, () -> {
            //When
            movieRepository.getMovieByTitle(MOVIE_TITLE);
        });
    }

    @Test
    void testGetMovieByTitleWithMovieMalformedException() {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.of(MOVIE_ENTITY_INVALID));

        //Then
        assertThrows(MovieMalformedException.class, () -> {
            //When
            movieRepository.getMovieByTitle(MOVIE_TITLE);
        });
    }

    @Test
    void testUpdateMovieWithoutError() throws MovieNotFoundException {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.of(MOVIE_ENTITY));

        //When
        movieRepository.updateMovie(UPDATE_MOVIE);

        //Then
        ArgumentCaptor<MovieEntity> movieEntityArgumentCaptor = ArgumentCaptor.forClass(MovieEntity.class);
        verify(movieDao, times(1)).save(movieEntityArgumentCaptor.capture());
        MovieEntity actual = movieEntityArgumentCaptor.getValue();
        assertThat(actual, equalTo(UPDATE_MOVIE_ENTITY));
    }

    @Test
    void testUpdateMovieWithMovieNotFoundException() {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.empty());

        //Then
        assertThrows(MovieNotFoundException.class, () -> {
            //When
            movieRepository.updateMovie(UPDATE_MOVIE);
        });
    }

    @Test
    void deleteMovieByTitleWithoutError() throws MovieNotFoundException {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.of(MOVIE_ENTITY));

        //When
        movieRepository.deleteMovieByTitle(MOVIE_TITLE);

        //Then
        ArgumentCaptor<String> movieEntityArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(movieDao, times(1)).deleteById(movieEntityArgumentCaptor.capture());
        String actual = movieEntityArgumentCaptor.getValue();
        assertThat(actual, equalTo(MOVIE_TITLE));
    }

    @Test
    void deleteMovieByTitleWithMovieNotFoundException() throws MovieNotFoundException {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.empty());

        //Then
        assertThrows(MovieNotFoundException.class, () -> {
            //When
            movieRepository.deleteMovieByTitle(MOVIE_TITLE);
        });
    }
}