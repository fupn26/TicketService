package com.epam.training.ticketservice.dao.entity;

import com.epam.training.ticketservice.dao.entity.id.ScreeningPriceComponentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScreeningPriceComponentEntity {

    @EmbeddedId
    private ScreeningPriceComponentId id;
}
