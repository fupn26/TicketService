package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dataccess.entity.ScreeningEntity;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;
import com.epam.training.ticketservice.repository.mapper.MovieMapper;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import com.epam.training.ticketservice.repository.mapper.RoomMapper;
import com.epam.training.ticketservice.repository.mapper.ScreeningMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScreeningMapperImpl implements ScreeningMapper {

    private final MovieMapper movieMapper;
    private final RoomMapper roomMapper;
    private final PriceComponentMapper priceComponentMapper;

    @Override
    public Screening mapToScreening(ScreeningEntity screeningEntityToMap) throws InvalidMovieLengthException,
            InvalidColumnException, InvalidRowException {
        return new Screening(
                screeningEntityToMap.getId(),
                movieMapper.mapToMovie(screeningEntityToMap.getMovie()),
                roomMapper.mapToRoom(screeningEntityToMap.getRoom()),
                screeningEntityToMap.getDateTime(),
                priceComponentMapper.mapToPriceComponents(screeningEntityToMap.getPriceComponents())
        );
    }

    @Override
    public ScreeningEntity mapToScreeningEntity(Screening screeningToMap) {
        if (screeningToMap.getUuid() == null) {
            return new ScreeningEntity(
                    movieMapper.mapToMovieEntity(screeningToMap.getMovie()),
                    roomMapper.mapToRoomEntity(screeningToMap.getRoom()),
                    screeningToMap.getStartDate(),
                    priceComponentMapper.mapToPriceComponentEntities(screeningToMap.getPriceComponents())
            );
        } else {
            return new ScreeningEntity(
                    screeningToMap.getUuid(),
                    movieMapper.mapToMovieEntity(screeningToMap.getMovie()),
                    roomMapper.mapToRoomEntity(screeningToMap.getRoom()),
                    screeningToMap.getStartDate(),
                    priceComponentMapper.mapToPriceComponentEntities(screeningToMap.getPriceComponents())
            );
        }
    }
}
