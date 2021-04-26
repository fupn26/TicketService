package com.epam.training.ticketservice.dataccess;

import com.epam.training.ticketservice.dataccess.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDao extends JpaRepository<AccountEntity, String> {
}
