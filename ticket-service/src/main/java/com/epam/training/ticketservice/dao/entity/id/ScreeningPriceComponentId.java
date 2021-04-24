package com.epam.training.ticketservice.dao.entity.id;

import com.epam.training.ticketservice.dao.entity.PriceComponentEntity;
import com.epam.training.ticketservice.dao.entity.ScreeningEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ScreeningPriceComponentId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "screening_id")
    private ScreeningEntity screening;
    @ManyToOne
    @JoinColumn(name = "price_component_id")
    private PriceComponentEntity priceComponent;
}
