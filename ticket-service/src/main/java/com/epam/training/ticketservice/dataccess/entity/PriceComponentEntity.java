package com.epam.training.ticketservice.dataccess.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class PriceComponentEntity {

    @Id
    private String name;
    private int value;

    public PriceComponentEntity(String componentName, int componentValue) {
        this.name = componentName;
        this.value = componentValue;
    }
}
