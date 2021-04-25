package com.epam.training.ticketservice.dao.entity;

import com.epam.training.ticketservice.domain.PriceComponent;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

@Entity
//@AllArgsConstructor
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
    @OneToMany
    @Setter
    private Set<PriceComponentEntity> priceComponents;

    public MovieEntity(String title, String genre, int length, Set<PriceComponentEntity> priceComponents) {
        this.title = title;
        this.genre = genre;
        this.length = length;
        this.priceComponents = priceComponents;
    }
}
