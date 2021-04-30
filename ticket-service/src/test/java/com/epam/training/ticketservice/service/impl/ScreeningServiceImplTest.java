package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.domain.Movie;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.domain.Room;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.exception.ScreeningAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.service.exception.BreakTimeException;
import com.epam.training.ticketservice.service.exception.OverlappingScreeningException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceImplTest {

    @InjectMocks
    private ScreeningServiceImpl screeningService;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private RoomRepository roomRepository;

    private static final String MOVIE_TITLE = "best movie";
    private static final String MOVIE_GENRE = "drama";
    private static final int MOVIE_LENGTH = 20;
    private static final String ROOM_NAME = "best room";
    private static final int ROWS = 10;
    private static final int COLUMNS = 20;
    private static final LocalDateTime TIME = LocalDateTime.now();

    private static final Movie MOVIE = createMovie(MOVIE_GENRE, MOVIE_LENGTH, Set.of());

    private static Movie createMovie(String genre, int length, Set<PriceComponent> priceComponents) {
        Movie result = null;
        try {
            result = new Movie(ScreeningServiceImplTest.MOVIE_TITLE, genre, length, priceComponents);
        } catch (InvalidMovieLengthException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final Room ROOM = createRoom(ROOM_NAME, ROWS, COLUMNS, Set.of());

    private static Room createRoom(String roomName, int rows, int columns, Set<PriceComponent> priceComponents) {
        Room result = null;
        try {
            result = new Room(roomName, rows, columns, priceComponents);
        } catch (InvalidRowException | InvalidColumnException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final Screening SCREENING_1 = new Screening(MOVIE, ROOM, TIME);
    private static final Screening SCREENING_2 = new Screening(MOVIE, ROOM, TIME.plusMinutes(30));
    private static final Screening SCREENING_3 = new Screening(MOVIE, ROOM, TIME.plusMinutes(60));
    private static final Screening NEW_NOT_OVERLAPPING_SCREENING = new Screening(MOVIE, ROOM,
            TIME.plusMinutes(90));
    private static final Screening OVERLAPPING_SCREENING_START_OVERLAPPED = new Screening(MOVIE, ROOM,
            TIME.plusMinutes(35));
    private static final Screening OVERLAPPING_SCREENING_END_OVERLAPPED = new Screening(MOVIE, ROOM,
            TIME.plusMinutes(-10));
    private static final Screening OVERLAPPING_SCREENING_START_BREAK_OVERLAPPED = new Screening(MOVIE, ROOM,
            TIME.plusMinutes(85));
    private static final Screening OVERLAPPING_SCREENING_END_BREAK_OVERLAPPED = new Screening(MOVIE, ROOM,
            TIME.plusMinutes(-25));
    private static final List<Screening> SCREENINGS = List.of(SCREENING_1, SCREENING_2, SCREENING_3);

    @Test
    void testCreateScreeningWithValidNotOverlappingScreening() throws MovieNotFoundException, RoomNotFoundException,
            ScreeningAlreadyExistsException, OverlappingScreeningException, BreakTimeException {
        //Given
        when(movieRepository.getMovieByTitle(any())).thenReturn(MOVIE);
        when(roomRepository.getRoomByName(any())).thenReturn(ROOM);
        when(screeningRepository.getAllScreenings()).thenReturn(SCREENINGS);

        //When
        screeningService.createScreening(MOVIE_TITLE, ROOM_NAME, NEW_NOT_OVERLAPPING_SCREENING.getStartDate());

        //Then
        verify(screeningRepository, times(1)).createScreening(NEW_NOT_OVERLAPPING_SCREENING);
    }

    @Test
    void testCreateScreeningWithValidStartOverlappingScreeningThrowsOverlappingScreeningException()
            throws MovieNotFoundException, RoomNotFoundException {
        //Given
        when(movieRepository.getMovieByTitle(any())).thenReturn(MOVIE);
        when(roomRepository.getRoomByName(any())).thenReturn(ROOM);
        when(screeningRepository.getAllScreenings()).thenReturn(SCREENINGS);

        assertThrows(OverlappingScreeningException.class, () -> {
            //When
            screeningService.createScreening(MOVIE_TITLE, ROOM_NAME,
                    OVERLAPPING_SCREENING_START_OVERLAPPED.getStartDate());
        });
    }

    @Test
    void testCreateScreeningWithValidEndOverlappingScreeningThrowsOverlappingScreeningException()
            throws MovieNotFoundException, RoomNotFoundException {
        //Given
        when(movieRepository.getMovieByTitle(any())).thenReturn(MOVIE);
        when(roomRepository.getRoomByName(any())).thenReturn(ROOM);
        when(screeningRepository.getAllScreenings()).thenReturn(SCREENINGS);

        assertThrows(OverlappingScreeningException.class, () -> {
            //When
            screeningService.createScreening(MOVIE_TITLE, ROOM_NAME,
                    OVERLAPPING_SCREENING_END_OVERLAPPED.getStartDate());
        });
    }

    @Test
    void testCreateScreeningWithValidStartBreakOverlappingScreeningThrowsBreakTimeException()
            throws MovieNotFoundException, RoomNotFoundException {
        //Given
        when(movieRepository.getMovieByTitle(any())).thenReturn(MOVIE);
        when(roomRepository.getRoomByName(any())).thenReturn(ROOM);
        when(screeningRepository.getAllScreenings()).thenReturn(SCREENINGS);

        assertThrows(BreakTimeException.class, () -> {
            //When
            screeningService.createScreening(MOVIE_TITLE, ROOM_NAME,
                    OVERLAPPING_SCREENING_START_BREAK_OVERLAPPED.getStartDate());
        });
    }

    @Test
    void testCreateScreeningWithValidEndBreakOverlappingScreeningThrowsBreakTimeException()
            throws MovieNotFoundException, RoomNotFoundException {
        //Given
        when(movieRepository.getMovieByTitle(any())).thenReturn(MOVIE);
        when(roomRepository.getRoomByName(any())).thenReturn(ROOM);
        when(screeningRepository.getAllScreenings()).thenReturn(SCREENINGS);

        assertThrows(BreakTimeException.class, () -> {
            //When
            screeningService.createScreening(MOVIE_TITLE, ROOM_NAME,
                    OVERLAPPING_SCREENING_END_BREAK_OVERLAPPED.getStartDate());
        });
    }

    @Test
    void testCreateScreeningWithNonExistingMovieThrowsMovieNotFoundException()
            throws MovieNotFoundException{
        //Given
        when(movieRepository.getMovieByTitle(any())).thenThrow(MovieNotFoundException.class);

        assertThrows(MovieNotFoundException.class, () -> {
            //When
            screeningService.createScreening(MOVIE_TITLE, ROOM_NAME,
                    NEW_NOT_OVERLAPPING_SCREENING.getStartDate());
        });
    }

    @Test
    void testCreateScreeningWithNonExistingRoomThrowsRoomNotFoundException()
            throws MovieNotFoundException, RoomNotFoundException {
        //Given
        when(movieRepository.getMovieByTitle(any())).thenReturn(MOVIE);
        when(roomRepository.getRoomByName(any())).thenThrow(RoomNotFoundException.class);

        assertThrows(RoomNotFoundException.class, () -> {
            //When
            screeningService.createScreening(MOVIE_TITLE, ROOM_NAME,
                    NEW_NOT_OVERLAPPING_SCREENING.getStartDate());
        });
    }

    @Test
    void testCreateScreeningWithExistingScreeningThrowsScreeningAlreadyExistsException()
            throws MovieNotFoundException, RoomNotFoundException, ScreeningAlreadyExistsException {
        //Given
        when(movieRepository.getMovieByTitle(any())).thenReturn(MOVIE);
        when(roomRepository.getRoomByName(any())).thenReturn(ROOM);
        when(screeningRepository.getAllScreenings()).thenReturn(SCREENINGS);
        doThrow(ScreeningAlreadyExistsException.class)
                .when(screeningRepository)
                .createScreening(any());

        assertThrows(ScreeningAlreadyExistsException.class, () -> {
            //When
            screeningService.createScreening(MOVIE_TITLE, ROOM_NAME,
                    NEW_NOT_OVERLAPPING_SCREENING.getStartDate());
        });

        verify(screeningRepository, times(1)).createScreening(NEW_NOT_OVERLAPPING_SCREENING);
    }

    @Test
    void testDeleteScreeningWithExistingScreening() throws ScreeningNotFoundException {
        //When
        screeningService.deleteScreening(MOVIE_TITLE, ROOM_NAME, TIME);

        //Then
        verify(screeningRepository, times(1))
                .deleteScreeningByMovieTitleRoomNameDate(MOVIE_TITLE, ROOM_NAME, TIME);
    }

    @Test
    void testDeleteScreeningWithNonExistingScreeningThrowsScreeningNotFoundException()
            throws ScreeningNotFoundException {
        //Given
        doThrow(ScreeningNotFoundException.class)
                .when(screeningRepository)
                .deleteScreeningByMovieTitleRoomNameDate(any(), any(), any());

        //Then
        assertThrows(ScreeningNotFoundException.class, () -> {
            //When
            screeningService.deleteScreening(MOVIE_TITLE, ROOM_NAME, TIME);
        });

        verify(screeningRepository, times(1))
                .deleteScreeningByMovieTitleRoomNameDate(MOVIE_TITLE, ROOM_NAME, TIME);
    }

    @Test
    void testGetAllScreeningsWithExistingScreenings() {
        //Given
        when(screeningRepository.getAllScreenings()).thenReturn(SCREENINGS);

        //When
        List<Screening> actual = screeningService.getAllScreenings();

        //Then
        verify(screeningRepository, times(1)).getAllScreenings();
        assertThat(actual, equalTo(SCREENINGS));
    }

    @Test
    void testGetAllScreeningsWithoutExistingScreenings() {
        //Given
        when(screeningRepository.getAllScreenings()).thenReturn(List.of());

        //When
        List<Screening> actual = screeningService.getAllScreenings();

        //Then
        verify(screeningRepository, times(1)).getAllScreenings();
        assertThat(actual, equalTo(List.of()));
    }
}