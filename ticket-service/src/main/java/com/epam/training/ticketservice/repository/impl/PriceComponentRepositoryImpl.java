package com.epam.training.ticketservice.repository.impl;

import com.epam.training.ticketservice.dataccess.PriceComponentDao;
import com.epam.training.ticketservice.dataccess.entity.PriceComponentEntity;
import com.epam.training.ticketservice.domain.PriceComponent;
import com.epam.training.ticketservice.repository.PriceComponentRepository;
import com.epam.training.ticketservice.repository.exception.PriceComponentAlreadyExistsException;
import com.epam.training.ticketservice.repository.exception.PriceComponentNotFoundException;
import com.epam.training.ticketservice.repository.mapper.PriceComponentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceComponentRepositoryImpl implements PriceComponentRepository {

    private final PriceComponentDao priceComponentDao;
    private final PriceComponentMapper priceComponentMapper;

    @Override
    public void createPriceComponent(PriceComponent priceComponentToCreate)
            throws PriceComponentAlreadyExistsException {
        if (getPriceComponentEntity(priceComponentToCreate.getName())
                .isPresent()) {
            throw new PriceComponentAlreadyExistsException(String.format("Price component already exists: %s",
                    priceComponentToCreate.getName()));
        }
        priceComponentDao.save(priceComponentMapper.mapToPriceComponentEntity(priceComponentToCreate));
    }

    @Override
    public PriceComponent getPriceComponentByName(String priceComponentName) throws PriceComponentNotFoundException {
        Optional<PriceComponentEntity> priceComponentEntity = getPriceComponentEntity(priceComponentName);
        if (priceComponentEntity.isEmpty()) {
            throw new PriceComponentNotFoundException(String.format("Price component not found: %s",
                    priceComponentName));
        }
        return priceComponentMapper.mapToPriceComponent(priceComponentEntity.get());
    }

    private Optional<PriceComponentEntity> getPriceComponentEntity(String priceComponentName) {
        return priceComponentDao.findById(priceComponentName);
    }
}
