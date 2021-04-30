package com.epam.training.ticketservice.dataccess.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"room_id", "date_time"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScreeningEntity {

    @Id
    @GeneratedValue
    private UUID id;
    @Setter
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;
    @Setter
    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;
    @Setter
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    @Setter
    @OneToMany(fetch = FetchType.EAGER)
    private Set<PriceComponentEntity> priceComponents;

    public ScreeningEntity(MovieEntity movieEntity, RoomEntity roomEntity,
                           LocalDateTime startDateTime, Set<PriceComponentEntity> priceComponents) {
        movie = movieEntity;
        room = roomEntity;
        dateTime = startDateTime;
        this.priceComponents = priceComponents;
    }

    public ScreeningEntity(UUID screeningId, MovieEntity movieEntity, RoomEntity roomEntity,
                           LocalDateTime startDateTime, Set<PriceComponentEntity> screeningPriceComponents) {
        id = screeningId;
        movie = movieEntity;
        room = roomEntity;
        dateTime = startDateTime;
        priceComponents = screeningPriceComponents;
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
        ScreeningEntity that = (ScreeningEntity) o;
        return Objects.equals(id, that.id) && movie.equals(that.movie) && room.equals(that.room)
                && dateTime.equals(that.dateTime) && priceComponents.equals(that.priceComponents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movie, room, dateTime, priceComponents);
    }
}
