package com.epam.training.ticketservice.repository.mapper;

import com.epam.training.ticketservice.dataccess.entity.PriceComponentEntity;
import com.epam.training.ticketservice.domain.PriceComponent;

import java.util.Set;

public interface PriceComponentMapper {
    PriceComponent mapToPriceComponent(PriceComponentEntity priceComponentEntityToMap);

    PriceComponentEntity mapToPriceComponentEntity(PriceComponent priceComponentToMap);

    Set<PriceComponent> mapToPriceComponents(Set<PriceComponentEntity> priceComponentEntitiesToMap);

    Set<PriceComponentEntity> mapToPriceComponentEntities(Set<PriceComponent> priceComponentsToMap);
}
