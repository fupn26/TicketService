package com.epam.training.ticketservice.dao.entity.id;

import com.epam.training.ticketservice.dao.entity.PriceComponentEntity;
import com.epam.training.ticketservice.dao.entity.RoomEntity;
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
public class RoomPriceComponentId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;
    @ManyToOne
    @JoinColumn(name = "price_component_id")
    private PriceComponentEntity priceComponent;
}
