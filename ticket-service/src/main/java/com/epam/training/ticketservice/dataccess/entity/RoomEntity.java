package com.epam.training.ticketservice.dataccess.entity;

import lombok.*;

import javax.persistence.Entity;
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
    private int rows;
    @Min(1)
    @Setter
    private int columns;
    @OneToMany
    @Setter
    private Set<PriceComponentEntity> priceComponentEntities;

    public RoomEntity(String name, int rows, int columns,
                      Set<PriceComponentEntity> priceComponentEntities) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.priceComponentEntities = priceComponentEntities;
    }
}