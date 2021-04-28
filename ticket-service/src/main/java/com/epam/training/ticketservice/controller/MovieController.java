package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.domain.Account;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.MovieService;
import com.epam.training.ticketservice.service.exception.NoSignedInAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@Slf4j
public class MovieController {

    private final MovieService movieService;
    private final AccountService accountService;
    private final String noPrivilegedAccountMessage = "you are not a privileged user";
    private final String noSignedInAccountMessage = "you are not signed in";
    private final String movieCreationSuccess = "Movie created: %s";
    private final String movieUpdateSuccess = "Movie updated: %s";
    private final String movieDeleteSuccess = "Movie deleted: %s";
    private final String noMovie = "There are no movies at the moment";

    public MovieController(MovieService movieService, AccountService accountService) {
        this.movieService = movieService;
        this.accountService = accountService;
    }

    @ShellMethod(value = "Creates a movie", key = "create movie")
    @ShellMethodAvailability("isPrivilegedUserSignIn")
    public String creatMovie(String movieTitle, String genre, int movieLengthInMinutes) {
        String result;
        try {
            movieService.createMovie(movieTitle, genre, movieLengthInMinutes);
            result = String.format(movieCreationSuccess, movieTitle);
        } catch (MovieAlreadyExistsException | MovieMalformedException e) {
            result = e.getMessage();
        }
        return result;
    }

    @ShellMethod(value = "Lists existing movies", key = "list movies")
    public String listMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return movies.isEmpty()
                ? noMovie
                : movies.stream()
                .map(this::mapMovieToString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @ShellMethod(value = "Updates a movie", key = "update movie")
    @ShellMethodAvailability("isPrivilegedUserSignIn")
    public String updateMovie(String movieTitle, String genre, int movieLengthInMinutes) {
        String result;
        try {
            movieService.updateMovie(movieTitle, genre, movieLengthInMinutes);
            result = String.format(movieUpdateSuccess, movieTitle);
        } catch (MovieNotFoundException | MovieMalformedException e) {
            result = e.getMessage();
        }
        return result;
    }

    @ShellMethod(value = "Deletes a movie", key = "delete movie")
    @ShellMethodAvailability("isPrivilegedUserSignIn")
    public String deleteMovie(String movieTitle) {
        String result;
        try {
            movieService.deleteMovie(movieTitle);
            result = String.format(movieDeleteSuccess, movieTitle);
        } catch (MovieNotFoundException e) {
            result = e.getMessage();
        }
        return result;
    }

    public Availability isPrivilegedUserSignIn() {
        try {
            Account account = accountService.getSignedInAccount();
            return account.isPrivileged()
                    ? Availability.available()
                    : Availability.unavailable(noPrivilegedAccountMessage);
        } catch (NoSignedInAccountException e) {
            return Availability.unavailable(noSignedInAccountMessage);
        }
    }

    private String mapMovieToString(Movie movie) {
        return String.format("%s (%s, %d minutes)",
                movie.getTitle(),
                movie.getGenre(),
                movie.getLength());
    }
}
