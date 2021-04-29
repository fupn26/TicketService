package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public void createMovie(String title, String genre, int lengthInMinutes) throws MovieAlreadyExistsException,
            MovieMalformedException {
        try {
            Movie movieToCreate = new Movie(title, genre, lengthInMinutes);
            movieRepository.createMovie(movieToCreate);
        } catch (InvalidMovieLengthException e) {
            throw new MovieMalformedException(e.getMessage());
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.getAllMovies();
    }

    @Override
    public Movie getMovieByTitle(String title) throws MovieNotFoundException {
        return movieRepository.getMovieByTitle(title);
    }

    @Override
    public void updateMovie(String title, String genre, int lengthInMinutes) throws MovieNotFoundException,
            MovieMalformedException {
        try {
            Movie movieToUpdate = new Movie(title, genre, lengthInMinutes);
            movieRepository.updateMovie(movieToUpdate);
        } catch (InvalidMovieLengthException e) {
            throw new MovieMalformedException(e.getMessage());
        }
    }

    @Override
    public void deleteMovieByTitle(String title) throws MovieNotFoundException {
        movieRepository.deleteMovieByTitle(title);
    }
}
