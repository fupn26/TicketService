package com.epam.training.ticketservice.dataccess.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class PriceCategoryEntity {

    @Id
    private String categoryName;
    @Min(1)
    private int price;

    public PriceCategoryEntity(String categoryName, int price) {
        this.categoryName = categoryName;
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
        PriceCategoryEntity that = (PriceCategoryEntity) o;
        return price == that.price && categoryName.equals(that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName, price);
    }
}
