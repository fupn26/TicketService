package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;

import java.util.List;

public interface MovieService {
    void createMovie(String title, String genre, int lengthInMinutes) throws MovieAlreadyExistsException,
            MovieMalformedException;

    List<Movie> getAllMovies();

    Movie getMovieByTitle(String title) throws MovieNotFoundException;

    void updateMovie(String title, String genre, int lengthInMinutes) throws MovieNotFoundException,
            MovieMalformedException;

    void deleteMovieByTitle(String title) throws MovieNotFoundException;
}
