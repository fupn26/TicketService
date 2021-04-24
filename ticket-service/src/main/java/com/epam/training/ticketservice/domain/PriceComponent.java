package com.epam.training.ticketservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PriceComponent {
    private final String name;
    private final int value;
}
