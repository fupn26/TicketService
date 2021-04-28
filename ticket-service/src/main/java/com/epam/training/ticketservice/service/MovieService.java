package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;

import java.util.List;

public interface MovieService {
    void createMovie(String title, String genre, int lengthInMinutes) throws MovieAlreadyExistsException,
            MovieMalformedException;

    List<Movie> getAllMovies();

    void updateMovie(String title, String genre, int lengthInMinutes) throws MovieNotFoundException,
            MovieMalformedException;

    void deleteMovie(String title) throws MovieNotFoundException;
}
