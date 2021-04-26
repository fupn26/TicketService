package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.PriceComponentDao;
import com.epam.training.ticketservice.dataccess.entity.PriceComponentEntity;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.repository.exception.PriceComponentAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.PriceComponentNotFoundException;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class PriceComponentRepositoryImplTest {

    @Spy
    @InjectMocks
    private PriceComponentRepositoryImpl priceComponentRepository;
    @Mock
    private PriceComponentDao priceComponentDao;
    @Mock
    private PriceComponentMapper priceComponentMapper;

    private static final String PRICE_COMPONENT_NAME = "glamour";
    private static final int PRICE_COMPONENT_VALUE = -200;
    private static final PriceComponent PRICE_COMPONENT = new PriceComponent(PRICE_COMPONENT_NAME,
            PRICE_COMPONENT_VALUE);
    private static final PriceComponentEntity PRICE_COMPONENT_ENTITY
            = new PriceComponentEntity(PRICE_COMPONENT_NAME, PRICE_COMPONENT_VALUE);


    @Test
    void testCreatePriceComponentWithoutError() throws PriceComponentAlreadyExistsException {
        //Given
        when(priceComponentDao.findById(any())).thenReturn(Optional.empty());
        when(priceComponentMapper.mapToPriceComponentEntity(any())).thenReturn(PRICE_COMPONENT_ENTITY);

        //When
        priceComponentRepository.createPriceComponent(PRICE_COMPONENT);

        //Then
        ArgumentCaptor<PriceComponentEntity> actual
                = ArgumentCaptor.forClass(PriceComponentEntity.class);
        verify(priceComponentDao, times(1)).save(actual.capture());
        assertThat(PRICE_COMPONENT_ENTITY, equalTo(actual.getValue()));
    }

    @Test
    void testCreatePriceComponentWithPriceComponentAlreadyExistsException()
            throws PriceComponentAlreadyExistsException {
        //Given
        when(priceComponentDao.findById(any())).thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));

        //Then
        assertThrows(PriceComponentAlreadyExistsException.class, () -> {
            //When
            priceComponentRepository.createPriceComponent(PRICE_COMPONENT);
        });
    }

    @Test
    void testGetPriceComponentByNameWithoutError() throws PriceComponentNotFoundException {
        //Given
        when(priceComponentDao.findById(any())).thenReturn(Optional.of(PRICE_COMPONENT_ENTITY));
        when(priceComponentMapper.mapToPriceComponent(any())).thenReturn(PRICE_COMPONENT);

        //When
        PriceComponent actual = priceComponentRepository.getPriceComponentByName(PRICE_COMPONENT_NAME);

        //Then
        assertThat(PRICE_COMPONENT, equalTo(actual));
    }

    @Test
    void testGetPriceComponentByNameWithPriceComponentNotFoundException() {
        //Given
        when(priceComponentDao.findById(any())).thenReturn(Optional.empty());

        //Then
        assertThrows(PriceComponentNotFoundException.class, () -> {
            //When
            priceComponentRepository.getPriceComponentByName(PRICE_COMPONENT_NAME);
        });
    }
}