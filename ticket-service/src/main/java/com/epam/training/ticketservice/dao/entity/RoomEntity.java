package com.epam.training.ticketservice.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
@NoArgsConstructor
@Getter
public class RoomEntity {

    @Id
    private String name;
    @Min(1)
    private int rows;
    @Min(1)
    private int columns;

    public RoomEntity(String name, int rows, int columns) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }
}
