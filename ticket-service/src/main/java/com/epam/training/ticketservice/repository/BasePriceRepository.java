package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.repository.exception.InvalidPriceException;

public interface BasePriceRepository {
    void updateBasePrice(int newPrice) throws InvalidPriceException;

    int getBasePrice();
}
