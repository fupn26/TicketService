package com.epam.training.ticketservice.dataccess.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private List<SeatEntity> seats;
    private int price;

    public BookingEntity(ScreeningEntity screeningEntity, AccountEntity accountEntity,
                         List<SeatEntity> reservedSeatEntities, int price) {
        this.screening = screeningEntity;
        this.account = accountEntity;
        this.seats = reservedSeatEntities;
        this.price = price;
    }
}
