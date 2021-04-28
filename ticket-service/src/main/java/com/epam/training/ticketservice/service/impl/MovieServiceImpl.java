package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    @Override
    public void createMovie(String title, String genre, int lengthInMinutes) throws MovieAlreadyExistsException,
            MovieMalformedException {

    }

    @Override
    public List<Movie> getAllMovies() {
        return List.of();
    }

    @Override
    public void updateMovie(String title, String genre, int lengthInMinutes) throws MovieNotFoundException,
            MovieMalformedException {

    }

    @Override
    public void deleteMovie(String title) throws MovieNotFoundException {

    }
}
