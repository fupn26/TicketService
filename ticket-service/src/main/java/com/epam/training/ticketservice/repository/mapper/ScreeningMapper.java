package com.epam.training.ticketservice.repository.mapper;

import com.epam.training.ticketservice.dao.entity.ScreeningEntity;
import com.epam.training.ticketservice.domain.Screening;
import com.epam.training.ticketservice.domain.exception.InvalidColumnException;
import com.epam.training.ticketservice.domain.exception.InvalidMovieLengthException;
import com.epam.training.ticketservice.domain.exception.InvalidRowException;

public interface ScreeningMapper {
    Screening mapToScreening(ScreeningEntity screeningEntityToMap) throws InvalidMovieLengthException, InvalidColumnException, InvalidRowException;

    ScreeningEntity mapToScreeningEntity(Screening screeningToMap);
}
