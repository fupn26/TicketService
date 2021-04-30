package com.epam.training.ticketservice.dataccess.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"room_id", "row_num", "column_num"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SeatEntity {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
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

    public SeatEntity(UUID seatId, RoomEntity roomEntity, int rowOfSeat, int columnOfSeat) {
        room = roomEntity;
        row = rowOfSeat;
        column = columnOfSeat;
        id = seatId;
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
        SeatEntity that = (SeatEntity) o;
        return row == that.row && column == that.column && Objects.equals(id, that.id) && room.equals(that.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, room, row, column);
    }
}
