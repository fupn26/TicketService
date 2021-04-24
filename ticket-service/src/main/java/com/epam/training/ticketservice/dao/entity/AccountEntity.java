package com.epam.training.ticketservice.dao.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class AccountEntity {

    @Id
    private String username;
    private String password;
    private boolean privileged;
}
