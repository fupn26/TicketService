package com.epam.training.ticketservice.dataccess.entity.id;

import com.epam.training.ticketservice.dataccess.entity.ScreeningEntity;
import com.epam.training.ticketservice.dataccess.entity.SeatEntity;
import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class ReservedSeatId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private SeatEntity seat;
    @ManyToOne
    @JoinColumn(name = "screening_id")
    private ScreeningEntity screening;
}
