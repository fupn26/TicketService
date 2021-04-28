package com.epam.training.ticketservice.dataccess.entity;

import com.epam.training.ticketservice.dataccess.entity.id.ReservedSeatId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReservedSeatEntity {

    @EmbeddedId
    private ReservedSeatId id;
}
