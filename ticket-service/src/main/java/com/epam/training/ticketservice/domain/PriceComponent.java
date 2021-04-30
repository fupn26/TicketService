package com.epam.training.ticketservice.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
//@EqualsAndHashCode
public class PriceComponent {
    private final String name;
    private final int value;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PriceComponent that = (PriceComponent) o;
        return value == that.value && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
