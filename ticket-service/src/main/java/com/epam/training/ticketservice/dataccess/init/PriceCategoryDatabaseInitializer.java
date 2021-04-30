package com.epam.training.ticketservice.dataccess.init;

import com.epam.training.ticketservice.dataccess.PriceCategoryDao;
import com.epam.training.ticketservice.dataccess.entity.AccountEntity;
import com.epam.training.ticketservice.dataccess.entity.PriceCategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PriceCategoryDatabaseInitializer {

    private final PriceCategoryDao priceCategoryDao;

    @PostConstruct
    public void initDatabase() {
        Optional<PriceCategoryEntity> priceCategoryEntity =  priceCategoryDao.findById("base");
        if (priceCategoryEntity.isEmpty()) {
            priceCategoryDao.save(new PriceCategoryEntity("base", 1500));
        }
    }
}
