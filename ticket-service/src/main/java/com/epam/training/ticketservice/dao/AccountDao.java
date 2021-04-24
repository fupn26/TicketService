package com.epam.training.ticketservice.dao;

import com.epam.training.ticketservice.dao.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDao extends JpaRepository<AccountEntity, String> {
}
