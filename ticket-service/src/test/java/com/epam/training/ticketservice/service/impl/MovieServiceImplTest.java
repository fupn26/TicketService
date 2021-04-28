package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @InjectMocks
    private MovieServiceImpl movieService;
    @Mock
    private MovieRepository movieRepository;

    private static final String MOVIE_TITLE = "James Bond";
    private static final String MOVIE_GENRE = "action";
    private static final int MOVIE_LENGTH = 120;
    private static final int INVALID_MOVIE_LENGTH = 0;
    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, Set.of());
    private static final List<Movie> MOVIES = List.of(MOVIE, MOVIE);

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(MovieServiceImplTest.MOVIE_TITLE, genre, length, priceComponents);
        } catch (InvalidMovieLengthException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Test
    void testCreateMovieWithNonExistingMovie() throws MovieMalformedException, MovieAlreadyExistsException {
        //when
        movieService.createMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);

        //Then
        verify(movieRepository, times(1)).createMovie(MOVIE);
    }

    @Test
    void testCreateMovieWithInvalidMovieThrowsMovieMalformedException() {

        //Then
        assertThrows(MovieMalformedException.class, () -> {
            //when
            movieService.createMovie(MOVIE_TITLE, MOVIE_GENRE, INVALID_MOVIE_LENGTH);
        });
    }

    @Test
    void testCreateMovieWithValidExistingMovieThrowsMovieAlreadyExistsException() throws MovieAlreadyExistsException {
        //Given
        doThrow(MovieAlreadyExistsException.class)
                .when(movieRepository)
                .createMovie(any());

        //Then
        assertThrows(MovieAlreadyExistsException.class, () -> {
            //When
            movieService.createMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);
        });
    }

    @Test
    void testGetAllMoviesWithExistingMoviesReturnsNonEmptyList() {
        //Given
        when(movieRepository.getAllMovies())
                .thenReturn(MOVIES);

        //When
        List<Movie> actual = movieService.getAllMovies();

        //Then
        verify(movieRepository, times(1)).getAllMovies();
        assertThat(actual, equalTo(MOVIES));
    }

    @Test
    void testGetAllMoviesWithoutExistingMoviesReturnsEmptyList() {
        //Given
        when(movieRepository.getAllMovies())
                .thenReturn(MOVIES);

        //When
        List<Movie> actual = movieService.getAllMovies();

        //Then
        verify(movieRepository, times(1)).getAllMovies();
        assertThat(actual, equalTo(MOVIES));
    }

    @Test
    void testGetMovieByTitleWithExistingMovieReturnsMovie() throws MovieMalformedException,
            MovieNotFoundException {
        //Given
        when(movieRepository.getMovieByTitle(any()))
                .thenReturn(MOVIE);

        //When
        Movie actual = movieService.getMovieByTitle(MOVIE_TITLE);

        //Then
        verify(movieRepository, times(1)).getMovieByTitle(MOVIE_TITLE);
        assertThat(actual, equalTo(MOVIE));
    }

    @Test
    void testGetMovieByTitleWithInvalidMovieThrowsMovieNotFoundException() throws MovieMalformedException,
            MovieNotFoundException {
        //Given
        when(movieRepository.getMovieByTitle(any()))
                .thenThrow(MovieMalformedException.class);

        //Then
        assertThrows(MovieNotFoundException.class, () -> {
            //When
            movieService.getMovieByTitle(MOVIE_TITLE);
        });
    }

    @Test
    void testUpdateMovieWithExistingMovie() throws MovieNotFoundException, MovieMalformedException {
        //When
        movieService.updateMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);

        //Then
        verify(movieRepository, times(1)).updateMovie(MOVIE);
    }

    @Test
    void testUpdateMovieWithInvalidMovieThrowsMovieMalformedException() {
        //Then
        assertThrows(MovieMalformedException.class, () -> {
            //When
            movieService.updateMovie(MOVIE_TITLE, MOVIE_GENRE, INVALID_MOVIE_LENGTH);
        });
    }

    @Test
    void testUpdateMovieWithNonExistingMovieThrowsMovieNotFoundException() throws MovieNotFoundException {
        //Given
        doThrow(MovieNotFoundException.class)
                .when(movieRepository)
                .updateMovie(any());
        //Then
        assertThrows(MovieNotFoundException.class, () -> {
            //When
            movieService.updateMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);
        });
    }

    @Test
    void testDeleteMovieWithExistingMovie() throws MovieNotFoundException {
        //When
        movieService.deleteMovieByTitle(MOVIE_TITLE);

        //Then
        verify(movieRepository, times(1)).deleteMovieByTitle(MOVIE_TITLE);
    }

    @Test
    void testDeleteMovieWithNonExistingMovieThrowsMovieNotFoundException() throws MovieNotFoundException {
        //Given
        doThrow(MovieNotFoundException.class)
                .when(movieRepository)
                .deleteMovieByTitle(any());

        //Then
        assertThrows(MovieNotFoundException.class, () -> {
            //When
            movieService.deleteMovieByTitle(MOVIE_TITLE);
        });
    }
}