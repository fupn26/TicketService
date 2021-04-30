package com.epam.training.ticketservice.dataccess.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BookingEntity {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;
    @ManyToOne
    private ScreeningEntity screening;
    @ManyToOne
    private AccountEntity account;
    @OneToMany(fetch = FetchType.EAGER)
    private List<SeatEntity> seats;
    private int price;

    public BookingEntity(ScreeningEntity screeningEntity, AccountEntity accountEntity,
                         List<SeatEntity> reservedSeatEntities, int price) {
        this.screening = screeningEntity;
        this.account = accountEntity;
        this.seats = reservedSeatEntities;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (id == null) {
            return false;
        }
        BookingEntity that = (BookingEntity) o;
        return price == that.price && Objects.equals(id, that.id)
                && screening.equals(that.screening) && account.equals(that.account) && seats.equals(that.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, screening, account, seats, price);
    }
}
