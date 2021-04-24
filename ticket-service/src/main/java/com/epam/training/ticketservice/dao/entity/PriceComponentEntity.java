package com.epam.training.ticketservice.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class PriceComponentEntity {

    @Id
    private String name;
    private int value;

    public PriceComponentEntity(String componentName, int componentValue) {
        this.name = componentName;
        this.value = componentValue;
    }
}
