package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dao.PriceComponentDao;
import com.epam.training.ticketservice.dao.entity.PriceComponentEntity;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.repository.PriceComponentRepository;
import com.epam.training.ticketservice.repository.exception.PriceComponentAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.PriceComponentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceComponentRepositoryImpl implements PriceComponentRepository {

    private final PriceComponentDao priceComponentDao;

    @Override
    public void createPriceComponent(PriceComponent priceComponentToCreate)
            throws PriceComponentAlreadyExistsException {
        if (getPriceComponentEntity(priceComponentToCreate.getName())
                .isPresent()) {
            throw new PriceComponentAlreadyExistsException(String.format("Price component already exists: %s",
                    priceComponentToCreate.getName()));
        }
        priceComponentDao.save(mapToPriceComponentEntity(priceComponentToCreate));
    }

    @Override
    public PriceComponent getPriceComponentByName(String priceComponentName) throws PriceComponentNotFoundException {
        Optional<PriceComponentEntity> priceComponentEntity = getPriceComponentEntity(priceComponentName);
        if (priceComponentEntity.isEmpty()) {
            throw new PriceComponentNotFoundException(String.format("Price component not found: %s",
                    priceComponentName));
        }
        return mapToPriceComponent(priceComponentEntity.get());
    }

    private PriceComponentEntity mapToPriceComponentEntity(PriceComponent priceComponentToMap) {
        return new PriceComponentEntity(priceComponentToMap.getName(),
                priceComponentToMap.getValue());
    }

    private PriceComponent mapToPriceComponent(PriceComponentEntity priceComponentEntityToMap) {
        return new PriceComponent(priceComponentEntityToMap.getName(),
                priceComponentEntityToMap.getValue());
    }

    private Optional<PriceComponentEntity> getPriceComponentEntity(String priceComponentName) {
        return priceComponentDao.findById(priceComponentName);
    }
}
