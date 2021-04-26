package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.MovieDao;
import com.epam.training.ticketservice.dataccess.entity.MovieEntity;
import com.epam.training.ticketservice.dataccess.entity.PriceComponentEntity;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.mapper.MovieMapper;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @Mock
    private MovieMapper movieMapper;
    @Mock
    private PriceComponentMapper priceComponentMapper;

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

    private Movie movie;
    private Movie updateMovie;
    private List<Movie> movies;

    private MovieEntity movieEntity;
    private MovieEntity updateMovieEntity;
    private MovieEntity movieEntityInvalid;
    private List<MovieEntity> movieEntities;
    private List<MovieEntity> movieEntitiesInvalid;

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
        movie = createMovie(MOVIE_GENRE, MOVIE_LENGTH, PRICE_COMPONENTS);
        updateMovie = createMovie(UPDATE_MOVIE_GENRE,
                UPDATE_MOVIE_LENGTH, UPDATE_PRICE_COMPONENTS);
        movies = List.of(movie, movie, movie);
        movieEntity = new MovieEntity(MOVIE_TITLE, MOVIE_GENRE,
                MOVIE_LENGTH, PRICE_COMPONENT_ENTITIES);
        updateMovieEntity = new MovieEntity(MOVIE_TITLE, UPDATE_MOVIE_GENRE,
                UPDATE_MOVIE_LENGTH, UPDATE_PRICE_COMPONENT_ENTITIES);
        movieEntityInvalid = new MovieEntity(MOVIE_TITLE, MOVIE_GENRE,
                INVALID_MOVIE_LENGTH, PRICE_COMPONENT_ENTITIES);
        movieEntities = List.of(movieEntity, movieEntity, movieEntity);
        movieEntitiesInvalid = List.of(movieEntityInvalid,
                movieEntityInvalid, movieEntityInvalid);
    }

    @Test
    void testCreateMovieWithoutError() throws MovieAlreadyExistsException {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.empty());
        when(movieMapper.mapToMovieEntity(any())).thenReturn(movieEntity);

        //When
        movieRepository.createMovie(movie);

        //Then
        ArgumentCaptor<MovieEntity> movieEntityArgumentCaptor = ArgumentCaptor.forClass(MovieEntity.class);
        verify(movieDao, times(1)).save(movieEntityArgumentCaptor.capture());
        MovieEntity actual = movieEntityArgumentCaptor.getValue();
        assertThat(actual, equalTo(movieEntity));
    }

    @Test
    void testCreateMovieWithMovieAlreadyExitsException() {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.of(movieEntity));

        //Then
        assertThrows(MovieAlreadyExistsException.class, () -> {
            //When
            movieRepository.createMovie(movie);
        });
    }

    @Test
    void testGetAllMoviesWithoutError() throws InvalidMovieLengthException {
        //Given
        when(movieDao.findAll()).thenReturn(movieEntities);
        when(movieMapper.mapToMovie(any())).thenReturn(movie);

        //When
        List<Movie> actual = movieRepository.getAllMovies();

        //Then
        assertThat(actual, equalTo(movies));
    }

    @Test
    void testGetAllMoviesWithInvalidMovieLengthException() throws InvalidMovieLengthException {
        //Given
        when(movieDao.findAll()).thenReturn(movieEntitiesInvalid);
        when(movieMapper.mapToMovie(any())).thenThrow(new InvalidMovieLengthException(""));


        //When
        List<Movie> actual = movieRepository.getAllMovies();

        //Then
        assertThat(actual.isEmpty(), equalTo(true));
    }

    @Test
    void testGetMovieByTitleWithoutError() throws MovieMalformedException,
            MovieNotFoundException,
            InvalidMovieLengthException {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.of(movieEntity));
        when(movieMapper.mapToMovie(any())).thenReturn(movie);


        //When
        Movie actual = movieRepository.getMovieByTitle(MOVIE_TITLE);

        //Then
        assertThat(actual, equalTo(movie));
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
    void testGetMovieByTitleWithMovieMalformedException() throws InvalidMovieLengthException {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.of(movieEntityInvalid));
        when(movieMapper.mapToMovie(any())).thenThrow(new InvalidMovieLengthException(""));

        //Then
        assertThrows(MovieMalformedException.class, () -> {
            //When
            movieRepository.getMovieByTitle(MOVIE_TITLE);
        });
    }

    @Test
    void testUpdateMovieWithoutError() throws MovieNotFoundException {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.of(movieEntity));
        when(priceComponentMapper.mapToPriceComponentEntities(any()))
                .thenReturn(UPDATE_PRICE_COMPONENT_ENTITIES);

        //When
        movieRepository.updateMovie(updateMovie);

        //Then
        ArgumentCaptor<MovieEntity> movieEntityArgumentCaptor = ArgumentCaptor.forClass(MovieEntity.class);
        verify(movieDao, times(1)).save(movieEntityArgumentCaptor.capture());
        MovieEntity actual = movieEntityArgumentCaptor.getValue();
        assertThat(actual, equalTo(updateMovieEntity));
    }

    @Test
    void testUpdateMovieWithMovieNotFoundException() {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.empty());

        //Then
        assertThrows(MovieNotFoundException.class, () -> {
            //When
            movieRepository.updateMovie(updateMovie);
        });
    }

    @Test
    void deleteMovieByTitleWithoutError() throws MovieNotFoundException {
        //Given
        when(movieDao.findById(any())).thenReturn(Optional.of(movieEntity));

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