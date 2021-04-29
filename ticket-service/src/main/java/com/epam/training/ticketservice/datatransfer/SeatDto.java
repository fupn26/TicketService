package com.epam.training.ticketservice.datatransfer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class SeatDto {
    private final int rowNum;
    private final int columnNum;
}
