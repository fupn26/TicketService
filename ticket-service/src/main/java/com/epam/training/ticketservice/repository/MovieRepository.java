package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;

import java.util.List;

public interface MovieRepository {
    void createMovie(Movie movieToCreate) throws MovieAlreadyExistsException;

    List<Movie> getAllMovies();

    Movie getMovieByTitle(String movieTitle) throws MovieNotFoundException;

    void updateMovie(Movie movieToUpdate) throws MovieNotFoundException;

    void deleteMovieByTitle(String movieTitle) throws MovieNotFoundException;
}
