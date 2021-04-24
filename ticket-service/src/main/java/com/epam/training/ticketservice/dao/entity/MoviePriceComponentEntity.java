package com.epam.training.ticketservice.dao.entity;

import com.epam.training.ticketservice.dao.entity.id.MoviePriceComponentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MoviePriceComponentEntity {

    @EmbeddedId
    private MoviePriceComponentId id;
}
