package com.epam.training.ticketservice.controller;

import com.epam.training.ticketservice.controller.availability.ShellMethodAvailabilityChecker;
import com.epam.training.ticketservice.controller.mapper.DateTimeMapper;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.repository.exception.MovieNotFoundException;
import com.epam.training.ticketservice.repository.exception.RoomNotFoundException;
import com.epam.training.ticketservice.repository.exception.ScreeningAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.ScreeningNotFoundException;
import com.epam.training.ticketservice.service.ScreeningService;
import com.epam.training.ticketservice.service.exception.BreakTimeException;
import com.epam.training.ticketservice.service.exception.OverlappingScreeningException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class ScreeningController {

    private final ScreeningService screeningService;
    private final ShellMethodAvailabilityChecker shellMethodAvailabilityChecker;
    private final DateTimeMapper dateTimeMapper;
    private final String screeningCreationSuccessTemplate = "Screening created: %s, %s, %s";
    private final String screeningDeletionSuccessTemplate = "Screening deleted: %s, %s, %s";
    private final String screeningOverlapping = "There is an overlapping screening";
    private final String screeningInBreakTime
            = "This would start in the break period after another screening in this room";
    private final String screeningTimeInvalid = "Invalid screen date time. Use this format: yyyy-MM-dd HH:mm";
    private final String noScreenings = "There are no screenings";
    private final String screeningStringTemplate
            = "%s (%s, %d minutes), screened in room %s, at %s";
    private final String dateTimeFormat = "yyyy-MM-dd HH:mm";


    public ScreeningController(ScreeningService screeningService,
                               ShellMethodAvailabilityChecker shellMethodAvailabilityChecker,
                               DateTimeMapper dateTimeMapper) {
        this.screeningService = screeningService;
        this.shellMethodAvailabilityChecker = shellMethodAvailabilityChecker;
        this.dateTimeMapper = dateTimeMapper;
    }

    @ShellMethod(value = "Creates a screening", key = "create screening")
    @ShellMethodAvailability("isPrivilegedUserSignedIn")
    public String createScreening(String movieName, String roomName, String screeningDateTime) {
        String result = null;
        try {
            screeningService.createScreening(movieName, roomName,
                    dateTimeMapper.mapToLocalDateTime(screeningDateTime, dateTimeFormat));
            result = String.format(screeningCreationSuccessTemplate, movieName, roomName, screeningDateTime);
        } catch (ScreeningAlreadyExistsException | MovieNotFoundException
                | RoomNotFoundException e) {
            result = e.getMessage();
        } catch (OverlappingScreeningException e) {
            result = screeningOverlapping;
        } catch (BreakTimeException e) {
            result = screeningInBreakTime;
        } catch (DateTimeParseException e) {
            result = screeningTimeInvalid;
        }
        return result;
    }

    @ShellMethod(value = "Lists existing screenings", key = "list screenings")
    public String listScreenings() {
        String result;
        List<Screening> screenings = screeningService.getAllScreenings();
        if (screenings.isEmpty()) {
            result = noScreenings;
        } else {
            result = screenings.stream()
                    .map(this::mapScreeningToString)
                    .collect(Collectors.joining(System.lineSeparator()));
        }
        return result;
    }

    @ShellMethod(value = "Deletes a screening", key = "delete screening")
    @ShellMethodAvailability("isPrivilegedUserSignedIn")
    public String deleteScreening(String movieTitle, String roomName, String screeningDateTime) {
        String result;
        try {
            screeningService.deleteScreening(movieTitle, roomName,
                    dateTimeMapper.mapToLocalDateTime(screeningDateTime, dateTimeFormat));
            result = String.format(screeningDeletionSuccessTemplate, movieTitle, roomName, screeningDateTime);
        } catch (ScreeningNotFoundException e) {
            result = e.getMessage();
        } catch (DateTimeParseException e) {
            result = screeningTimeInvalid;
        }
        return result;
    }

    public Availability isPrivilegedUserSignedIn() {
        return shellMethodAvailabilityChecker.isPrivilegedAccountSignIn();
    }

    private String mapScreeningToString(Screening screening) {
        return String.format(screeningStringTemplate,
                screening.getMovie().getTitle(),
                screening.getMovie().getGenre(),
                screening.getMovie().getLength(),
                screening.getRoom().getName(),
                dateTimeMapper.mapToString(screening.getStartDate(), dateTimeFormat));
    }
}
