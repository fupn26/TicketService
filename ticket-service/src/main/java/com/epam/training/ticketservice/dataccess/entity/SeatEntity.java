package com.epam.training.ticketservice.dataccess.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
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
import javax.validation.constraints.Min;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"room_id", "row_num", "column_num"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class SeatEntity {

    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;
    @Min(1)
    @Column(name = "row_num")
    private int row;
    @Min(1)
    @Column(name = "column_num")
    private int column;

    public SeatEntity(RoomEntity roomEntity, int rowOfSeat, int columnOfSeat) {
        room = roomEntity;
        row = rowOfSeat;
        column = columnOfSeat;
    }
}
