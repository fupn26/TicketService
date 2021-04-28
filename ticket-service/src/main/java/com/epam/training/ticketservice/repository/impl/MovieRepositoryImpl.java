package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.MovieDao;
import com.epam.training.ticketservice.dataccess.entity.MovieEntity;
import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.exception.MovieAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.MovieMalformedException;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.mapper.MovieMapper;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepository {

    private final MovieDao movieDao;
    private final MovieMapper movieMapper;
    private final PriceComponentMapper priceComponentMapper;

    @Override
    public void createMovie(Movie movieToCreate) throws MovieAlreadyExistsException {
        if (isMovieExistsByTitle(movieToCreate.getTitle())) {
            throw new MovieAlreadyExistsException(String.format("Movie already exists: %s",
                    movieToCreate.getTitle()));
        }
        movieDao.save(movieMapper.mapToMovieEntity(movieToCreate));
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieDao.findAll().stream()
                .map(this::mapToMovie)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Movie getMovieByTitle(String movieTitle) throws MovieNotFoundException, MovieMalformedException {
        Optional<Movie> movie = mapToMovie(getMovieEntityByTitle(movieTitle));
        if (movie.isEmpty()) {
            throw new MovieMalformedException(String.format("Movie found but malformed: %s", movieTitle));
        }
        return movie.get();
    }

    @Override
    public void updateMovie(Movie movieToUpdate) throws MovieNotFoundException {
        MovieEntity movieEntityToUpdate = getMovieEntityByTitle(movieToUpdate.getTitle());
        movieEntityToUpdate.setGenre(movieToUpdate.getGenre());
        movieEntityToUpdate.setLength(movieToUpdate.getLength());
        movieEntityToUpdate.setPriceComponents(
                priceComponentMapper.mapToPriceComponentEntities(movieToUpdate.getPriceComponentSet())
        );
        movieDao.save(movieEntityToUpdate);
    }

    @Override
    public void deleteMovieByTitle(String movieTitle) throws MovieNotFoundException {
        if (!isMovieExistsByTitle(movieTitle)) {
            throw new MovieNotFoundException(String.format("Movie not found: %s", movieTitle));
        }
        movieDao.deleteById(movieTitle);
    }

    private Optional<Movie> mapToMovie(MovieEntity movieEntityToMap) {
        Optional<Movie> result = Optional.empty();
        try {
            result = Optional.of(movieMapper.mapToMovie(movieEntityToMap));
        } catch (InvalidMovieLengthException e) {
            log.warn(e.getMessage());
        }
        return result;
    }

    private boolean isMovieExistsByTitle(String movieTitle) {
        Optional<MovieEntity> movieEntity = movieDao.findById(movieTitle);
        return movieEntity.isPresent();
    }

    private MovieEntity getMovieEntityByTitle(String movieTitle) throws MovieNotFoundException {
        Optional<MovieEntity> movieEntity = movieDao.findById(movieTitle);
        if (movieEntity.isEmpty()) {
            throw new MovieNotFoundException(String.format("Movie not found: %s", movieTitle));
        }
        return movieEntity.get();
    }
}
