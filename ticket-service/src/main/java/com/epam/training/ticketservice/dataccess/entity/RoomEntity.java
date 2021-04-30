package com.epam.training.ticketservice.dataccess.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class RoomEntity {

    @Id
    private String name;
    @Min(1)
    @Setter
    private int roomRows;
    @Min(1)
    @Setter
    private int roomColumns;
    @OneToMany(fetch = FetchType.EAGER)
    @Setter
    private Set<PriceComponentEntity> priceComponentEntities;

    public RoomEntity(String name, int rows, int columns,
                      Set<PriceComponentEntity> priceComponentEntities) {
        this.name = name;
        this.roomRows = rows;
        this.roomColumns = columns;
        this.priceComponentEntities = priceComponentEntities;
    }
}
