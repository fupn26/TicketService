package com.epam.training.ticketservice.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class BookingEntity {

    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private ScreeningEntity screening;
    @ManyToOne
    private AccountEntity account;
    @OneToMany
    private Set<ReservedSeatEntity> seats;
    private int price;

    public BookingEntity(ScreeningEntity screeningEntity, AccountEntity accountEntity,
                         Set<ReservedSeatEntity> reservedSeatEntities, int price) {
        this.screening = screeningEntity;
        this.account = accountEntity;
        this.seats = reservedSeatEntities;
        this.price = price;
    }
}