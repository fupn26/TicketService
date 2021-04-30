package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.PriceCategoryDao;
import com.epam.training.ticketservice.dataccess.entity.PriceCategoryEntity;
import com.epam.training.ticketservice.repository.exception.InvalidPriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasePriceRepositoryImplTest {

    @InjectMocks
    private BasePriceRepositoryImpl basePriceRepository;
    @Mock
    private PriceCategoryDao priceCategoryDao;

    private int price;
    private int newPrice;
    private int invalidPrice;
    private PriceCategoryEntity priceCategoryEntity;
    private PriceCategoryEntity newPriceCategoryEntity;


    @BeforeEach
    void init() {
        price = 1500;
        newPrice = 1300;
        invalidPrice = 0;
        priceCategoryEntity = new PriceCategoryEntity("base", 1500);
        newPriceCategoryEntity = new PriceCategoryEntity("base", newPrice);
    }

    @Test
    void testUpdateBasePriceWithValidNewPrice() throws InvalidPriceException {
        //Given
        when(priceCategoryDao.findById(any())).thenReturn(Optional.of(priceCategoryEntity));

        //When
        basePriceRepository.updateBasePrice(newPrice);

        //Then
        verify(priceCategoryDao, times(1)).save(newPriceCategoryEntity);
    }

    @Test
    void testUpdateBasePriceWithInvalidNewPriceThrowsInvalidPriceException() throws InvalidPriceException {
        //Then
        assertThrows(InvalidPriceException.class, () -> {
            //When
            basePriceRepository.updateBasePrice(invalidPrice);
        });
    }

    @Test
    void testGetBasePrice() {
        //Given
        when(priceCategoryDao.findById(any())).thenReturn(Optional.of(priceCategoryEntity));

        //When
        int actual = basePriceRepository.getBasePrice();

        //Then
        assertThat(actual, equalTo(price));
        verify(priceCategoryDao, times(1)).findById(priceCategoryEntity.getCategoryName());
    }
}