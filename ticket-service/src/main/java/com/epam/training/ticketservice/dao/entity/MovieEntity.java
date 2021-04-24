package com.epam.training.ticketservice.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MovieEntity {

    @Id
    private String title;
    private String genre;
    @Min(1)
    private int length;
}
