package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.controller.availability.ShellMethodAvailabilityChecker;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.service.MovieService;
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
    private final ShellMethodAvailabilityChecker shellMethodAvailability;
    private final String movieCreationSuccessTemplate = "Movie created: %s";
    private final String movieUpdateSuccessTemplate = "Movie updated: %s";
    private final String movieDeleteSuccessTemplate = "Movie deleted: %s";
    private final String noMovie = "There are no movies at the moment";
    private final String movieStringTemplate = "%s (%s, %d minutes)";

    public MovieController(MovieService movieService, ShellMethodAvailabilityChecker shellMethodAvailability) {
        this.movieService = movieService;
        this.shellMethodAvailability = shellMethodAvailability;
    }

    @ShellMethod(value = "Creates a movie", key = "create movie")
    @ShellMethodAvailability("isPrivilegedAccountSignIn")
    public String creatMovie(String movieTitle, String genre, int movieLengthInMinutes) {
        String result;
        try {
            movieService.createMovie(movieTitle, genre, movieLengthInMinutes);
            result = String.format(movieCreationSuccessTemplate, movieTitle);
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
    @ShellMethodAvailability("isPrivilegedAccountSignIn")
    public String updateMovie(String movieTitle, String genre, int movieLengthInMinutes) {
        String result;
        try {
            movieService.updateMovie(movieTitle, genre, movieLengthInMinutes);
            result = String.format(movieUpdateSuccessTemplate, movieTitle);
        } catch (MovieNotFoundException | MovieMalformedException e) {
            result = e.getMessage();
        }
        return result;
    }

    @ShellMethod(value = "Deletes a movie", key = "delete movie")
    @ShellMethodAvailability("isPrivilegedAccountSignIn")
    public String deleteMovie(String movieTitle) {
        String result;
        try {
            movieService.deleteMovieByTitle(movieTitle);
            result = String.format(movieDeleteSuccessTemplate, movieTitle);
        } catch (MovieNotFoundException e) {
            result = e.getMessage();
        }
        return result;
    }

    public Availability isPrivilegedAccountSignIn() {
        return shellMethodAvailability.isPrivilegedAccountSignIn();
    }

    private String mapMovieToString(Movie movie) {
        return String.format(movieStringTemplate,
                movie.getTitle(),
                movie.getGenre(),
                movie.getLength());
    }
}
