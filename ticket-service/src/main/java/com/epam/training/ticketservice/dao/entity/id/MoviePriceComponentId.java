package com.epam.training.ticketservice.dao.entity.id;

import com.epam.training.ticketservice.dao.entity.MovieEntity;
import com.epam.training.ticketservice.dao.entity.PriceComponentEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class MoviePriceComponentId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;
    @ManyToOne
    @JoinColumn(name = "price_component_id")
    private PriceComponentEntity priceComponent;
}