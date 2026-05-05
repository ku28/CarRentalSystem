package com.carrental.carrentalsystem.dto;

import com.carrental.carrentalsystem.enums.AvailabilityStatus;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CarResponse {

    private Long id;
    private String model;
    private String brand;
    private String category;
    private BigDecimal pricePerDay;
    private AvailabilityStatus availabilityStatus;
}
