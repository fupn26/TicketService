package com.epam.training.ticketservice.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"room_id", "date_time"})})
@NoArgsConstructor
@Getter
public class ScreeningEntity {

    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    public ScreeningEntity(MovieEntity movieEntity, RoomEntity roomEntity, LocalDateTime startDateTime) {
        movie = movieEntity;
        room = roomEntity;
        dateTime = startDateTime;
    }
}
