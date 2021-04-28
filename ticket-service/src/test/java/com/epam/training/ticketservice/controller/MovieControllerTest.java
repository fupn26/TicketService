package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.MovieService;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.shell.Availability;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;
    @Mock
    private AccountService accountService;
    @Mock
    private MovieService movieService;

    private static final String ACCOUNT_NAME = "james";
    private static final String ACCOUNT_PASSWORD = "bond";
    private static final String MOVIE_TITLE = "James Bond";
    private static final String MOVIE_GENRE = "action";
    private static final int MOVIE_LENGTH = 120;
    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, Set.of());
    private static final List<Movie> MOVIES = List.of(MOVIE, MOVIE);
    private static final String MOVIE_CREATED_MESSAGE = "Movie created: %s";
    private static final String MOVIE_UPDATED_MESSAGE = "Movie updated: %s";
    private static final String MOVIE_DELETED_MESSAGE = "Movie deleted: %s";
    private static final String MOVIE_EXISTS_MESSAGE = "Movie already exists";
    private static final String MOVIE_NOT_FOUND = "Movie not found";
    private static final String MOVIE_MALFORMED = "Movie length invalid";
    private static final String NO_PRIVILEGED_ACCOUNT_MESSAGE = "you are not a privileged user";
    private static final String NO_SIGNED_IN_ACCOUNT_MESSAGE = "you are not signed in";
    private static final String NO_MOVIES = "There are no movies at the moment";
    private static final String MOVIE_STRING_TEMPLATE = "%s (%s, %d minutes)";
    private static final List<String> MOVIE_STRING_LIST = List.of(
            String.format(MOVIE_STRING_TEMPLATE, MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH),
            String.format(MOVIE_STRING_TEMPLATE, MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH));
    private static final MovieAlreadyExistsException MOVIE_ALREADY_EXISTS_EXCEPTION
            = new MovieAlreadyExistsException(MOVIE_EXISTS_MESSAGE);
    private static final MovieNotFoundException MOVIE_NOT_FOUND_EXCEPTION
            = new MovieNotFoundException(MOVIE_NOT_FOUND);
    private static final MovieMalformedException MOVIE_MALFORMED_EXCEPTION
            = new MovieMalformedException(MOVIE_MALFORMED);
    private static final Account ACCOUNT = new Account(ACCOUNT_NAME, ACCOUNT_PASSWORD, false);
    private static final Account ACCOUNT_PRIVILEGED = new Account(ACCOUNT_NAME, ACCOUNT_PASSWORD, true);

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(MovieControllerTest.MOVIE_TITLE, genre, length, priceComponents);
        } catch (InvalidMovieLengthException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Test
    void testCreatMovieWithNonExistingValidMovie() throws MovieMalformedException,
            MovieAlreadyExistsException {
        //When
        String actual = movieController.creatMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);

        //Then
        verify(movieService, times(1)).createMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);
        assertThat(actual, equalTo(String.format(MOVIE_CREATED_MESSAGE, MOVIE_TITLE)));
    }

    @Test
    void testCreatMovieWithExistingValidMovie() throws MovieMalformedException,
            MovieAlreadyExistsException {
        //Given
        doThrow(MOVIE_ALREADY_EXISTS_EXCEPTION)
                .when(movieService)
                .createMovie(any(), any(), anyInt());
        //When
        String actual = movieController.creatMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);

        //Then
        verify(movieService, times(1)).createMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);
        assertThat(actual, equalTo(MOVIE_ALREADY_EXISTS_EXCEPTION.getMessage()));
    }

    @Test
    void testCreatMovieWithInValidMovie() throws MovieMalformedException,
            MovieAlreadyExistsException {
        //Given
        doThrow(MOVIE_MALFORMED_EXCEPTION)
                .when(movieService)
                .createMovie(any(), any(), anyInt());
        //When
        String actual = movieController.creatMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);

        //Then
        verify(movieService, times(1)).createMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);
        assertThat(actual, equalTo(MOVIE_MALFORMED_EXCEPTION.getMessage()));
    }

    @Test
    void testIsPrivilegedUserSignInWithSignedInAccountPrivileged() throws NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT_PRIVILEGED);

        //When
        Availability actual = movieController.isPrivilegedUserSignIn();

        //Then
        verify(accountService, times(1)).getSignedInAccount();
        assertThat(actual.isAvailable(), equalTo(true));
    }

    @Test
    void testIsPrivilegedUserSignInWithSignedInAccountNonPrivileged() throws NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenReturn(ACCOUNT);

        //When
        Availability actual = movieController.isPrivilegedUserSignIn();

        //Then
        verify(accountService, times(1)).getSignedInAccount();
        assertThat(actual.getReason(), equalTo(NO_PRIVILEGED_ACCOUNT_MESSAGE));
    }

    @Test
    void testIsPrivilegedUserSignInWithoutSignedInAccount() throws NoSignedInAccountException {
        //Given
        when(accountService.getSignedInAccount()).thenThrow(NoSignedInAccountException.class);

        //When
        Availability actual = movieController.isPrivilegedUserSignIn();

        //Then
        verify(accountService, times(1)).getSignedInAccount();
        assertThat(actual.getReason(), equalTo(NO_SIGNED_IN_ACCOUNT_MESSAGE));
    }

    @Test
    void testUpdateMovieWithExistingValidMovie() throws MovieMalformedException, MovieNotFoundException {
        //When
        String actual = movieController.updateMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);

        //Then
        verify(movieService, times(1)).updateMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);
        assertThat(actual, equalTo(String.format(MOVIE_UPDATED_MESSAGE, MOVIE_TITLE)));
    }

    @Test
    void testUpdateMovieWithNonExistingValidMovie() throws MovieMalformedException, MovieNotFoundException {
        //Given
        doThrow(MOVIE_NOT_FOUND_EXCEPTION)
                .when(movieService)
                .updateMovie(any(), any(), anyInt());
        //When
        String actual = movieController.updateMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);

        //Then
        verify(movieService, times(1)).updateMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);
        assertThat(actual, equalTo(MOVIE_NOT_FOUND));
    }

    @Test
    void testUpdateMovieWithNonValidMovie() throws MovieMalformedException, MovieNotFoundException {
        //Given
        doThrow(MOVIE_MALFORMED_EXCEPTION)
                .when(movieService)
                .updateMovie(any(), any(), anyInt());
        //When
        String actual = movieController.updateMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);

        //Then
        verify(movieService, times(1)).updateMovie(MOVIE_TITLE, MOVIE_GENRE, MOVIE_LENGTH);
        assertThat(actual, equalTo(MOVIE_MALFORMED));
    }

    @Test
    void testListMoviesWithExistingMovies() {
        //Given
        when(movieService.getAllMovies()).thenReturn(MOVIES);

        //When
        String actual = movieController.listMovies();

        //Given
        verify(movieService, times(1)).getAllMovies();
        assertThat(actual, equalTo(
                MOVIE_STRING_LIST.stream().collect(Collectors.joining(System.lineSeparator()))
        ));
    }
    @Test

    void testListMoviesWithoutExistingMovie() {
        //Given
        when(movieService.getAllMovies()).thenReturn(List.of());

        //When
        String actual = movieController.listMovies();

        //Given
        verify(movieService, times(1)).getAllMovies();
        assertThat(actual, equalTo(NO_MOVIES));
    }

    @Test
    void testDeleteMovieWithExistingMovie() throws MovieNotFoundException {
        //When
        String actual = movieController.deleteMovie(MOVIE_TITLE);

        //Then
        verify(movieService, times(1)).deleteMovieByTitle(MOVIE_TITLE);
        assertThat(actual, equalTo(String.format(MOVIE_DELETED_MESSAGE, MOVIE_TITLE)));
    }

    @Test
    void testDeleteMovieWithNonExistingMovie() throws MovieNotFoundException {
        //Given
        doThrow(MOVIE_NOT_FOUND_EXCEPTION)
                .when(movieService)
                .deleteMovieByTitle(any());

        //When
        String actual = movieController.deleteMovie(MOVIE_TITLE);

        //Then
        verify(movieService, times(1)).deleteMovieByTitle(MOVIE_TITLE);
        assertThat(actual, equalTo(MOVIE_NOT_FOUND_EXCEPTION.getMessage()));
    }
}
