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
public class MovieEntity {

    @Id
    private String title;
    @Setter
    private String genre;
    @Min(1)
    @Setter
    private int length;
    @OneToMany(fetch = FetchType.EAGER)
    @Setter
    private Set<PriceComponentEntity> priceComponents;

    public MovieEntity(String title, String genre, int length, Set<PriceComponentEntity> priceComponents) {
        this.title = title;
        this.genre = genre;
        this.length = length;
        this.priceComponents = priceComponents;
    }
}
