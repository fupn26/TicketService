package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.dao.entity.PriceComponentEntity;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.repository.exception.PriceComponentAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.PriceComponentNotFoundException;

public interface PriceComponentRepository {
    void createPriceComponent(PriceComponent priceComponentToCreate) throws PriceComponentAlreadyExistsException;

    PriceComponent getPriceComponentByName(String priceComponentName) throws PriceComponentNotFoundException;
}
