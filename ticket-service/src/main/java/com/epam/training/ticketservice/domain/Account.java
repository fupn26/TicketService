package com.epam.training.ticketservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Account {
    private final String userName;
    private final String password;
    private final boolean privileged;
}
