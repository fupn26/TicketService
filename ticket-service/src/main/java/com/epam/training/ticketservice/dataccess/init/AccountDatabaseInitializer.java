package com.epam.training.ticketservice.dataccess.init;

import com.epam.training.ticketservice.dataccess.AccountDao;
import com.epam.training.ticketservice.dataccess.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class AccountDatabaseInitializer {

    private final AccountDao accountDao;

    @PostConstruct
    public void initDatabase() {
        accountDao.save(new AccountEntity("admin", "admin", true));
    }
}
