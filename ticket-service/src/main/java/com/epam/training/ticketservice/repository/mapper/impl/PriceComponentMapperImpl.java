package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dataccess.entity.PriceComponentEntity;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PriceComponentMapperImpl implements PriceComponentMapper {
    @Override
    public PriceComponent mapToPriceComponent(PriceComponentEntity priceComponentEntityToMap) {
        return new PriceComponent(priceComponentEntityToMap.getName(),
                priceComponentEntityToMap.getValue());
    }

    @Override
    public PriceComponentEntity mapToPriceComponentEntity(PriceComponent priceComponentToMap) {
        return new PriceComponentEntity(priceComponentToMap.getName(),
                priceComponentToMap.getValue());
    }

    @Override
    public Set<PriceComponent> mapToPriceComponents(Set<PriceComponentEntity> priceComponentEntitiesToMap) {
        return priceComponentEntitiesToMap.stream()
                .map(this::mapToPriceComponent)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<PriceComponentEntity> mapToPriceComponentEntities(Set<PriceComponent> priceComponentsToMap) {
        return priceComponentsToMap.stream()
                .map(this::mapToPriceComponentEntity)
                .collect(Collectors.toSet());
    }
}
