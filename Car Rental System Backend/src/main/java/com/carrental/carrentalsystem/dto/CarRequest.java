package com.carrental.carrentalsystem.dto;

import com.carrental.carrentalsystem.enums.AvailabilityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarRequest {

    @NotBlank
    private String model;

    @NotBlank
    private String brand;

    @NotBlank
    private String category;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal pricePerDay;

    @NotNull
    private AvailabilityStatus availabilityStatus;
}
