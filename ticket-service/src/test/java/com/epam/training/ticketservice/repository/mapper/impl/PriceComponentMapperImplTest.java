package com.epam.training.ticketservice.repository.mapper.impl;

import com.epam.training.ticketservice.dao.entity.PriceComponentEntity;
import com.epam.training.ticketservice.domain.PriceComponent;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class PriceComponentMapperImplTest {

    private final PriceComponentMapperImpl priceComponentMapper = new PriceComponentMapperImpl();

    private static final String PRICE_COMPONENT_NAME = "glamour";
    private static final int PRICE_COMPONENT_VALUE = -200;

    private static final PriceComponent PRICE_COMPONENT = new PriceComponent(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponent> PRICE_COMPONENTS = Set.of(PRICE_COMPONENT);
    private static final PriceComponentEntity PRICE_COMPONENT_ENTITY = new PriceComponentEntity(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final Set<PriceComponentEntity> PRICE_COMPONENT_ENTITIES = Set.of(PRICE_COMPONENT_ENTITY);

    @Test
    void testMapToPriceComponentWithoutError() {
        //When
        PriceComponent actual = priceComponentMapper.mapToPriceComponent(PRICE_COMPONENT_ENTITY);

        //Then
        assertThat(actual, equalTo(PRICE_COMPONENT));
    }

    @Test
    void testMapToPriceComponentEntityWithoutError() {
        //When
        PriceComponentEntity actual = priceComponentMapper.mapToPriceComponentEntity(PRICE_COMPONENT);

        //Then
        assertThat(actual, equalTo(PRICE_COMPONENT_ENTITY));
    }

    @Test
    void testMapToPriceComponentsWithoutError() {
        //When
        Set<PriceComponent> actual = priceComponentMapper.mapToPriceComponents(PRICE_COMPONENT_ENTITIES);

        //Then
        assertThat(actual, equalTo(PRICE_COMPONENTS));
    }

    @Test
    void testMapToPriceComponentEntitiesWithoutError() {
        //When
        Set<PriceComponentEntity> actual = priceComponentMapper.mapToPriceComponentEntities(PRICE_COMPONENTS);

        //Then
        assertThat(actual, equalTo(PRICE_COMPONENT_ENTITIES));
    }
}