package com.epam.training.ticketservice.repository.mapper;

import com.epam.training.ticketservice.dataccess.entity.AccountEntity;
import com.epam.training.ticketservice.domain.Account;

public interface AccountMapper {
    Account mapToAccount(AccountEntity accountEntityToMap);

    AccountEntity mapToAccountEntity(Account accountToMap);
}
