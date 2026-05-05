package com.carrental.carrentalsystem.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {

    @NotNull
    private Long carId;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;
}
