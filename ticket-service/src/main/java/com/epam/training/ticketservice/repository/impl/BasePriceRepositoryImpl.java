package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.PriceCategoryDao;
import com.epam.training.ticketservice.dataccess.entity.PriceCategoryEntity;
import com.epam.training.ticketservice.repository.BasePriceRepository;
import com.epam.training.ticketservice.repository.exception.InvalidPriceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BasePriceRepositoryImpl implements BasePriceRepository {

    private final PriceCategoryDao priceCategoryDao;

    @Override
    public void updateBasePrice(int newPrice) throws InvalidPriceException {
        if (newPrice < 1) {
            throw new InvalidPriceException("Price can't be less than one");
        }
        Optional<PriceCategoryEntity> priceCategoryEntityOptional = priceCategoryDao.findById("base");
        if (priceCategoryEntityOptional.isEmpty()) {
            log.error("Base price not created");
        }
        PriceCategoryEntity priceCategoryEntity = priceCategoryEntityOptional.get();
        priceCategoryEntity.setPrice(newPrice);
        priceCategoryDao.save(priceCategoryEntity);
    }

    @Override
    public int getBasePrice() {
        Optional<PriceCategoryEntity> priceCategoryEntity = priceCategoryDao.findById("base");
        if (priceCategoryEntity.isEmpty()) {
            log.error("Base price not created");
        }
        return priceCategoryEntity.get().getPrice();
    }
}
