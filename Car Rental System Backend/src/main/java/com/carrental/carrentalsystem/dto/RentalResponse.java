package com.carrental.carrentalsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalResponse {

    private Long id;
    private Long bookingId;
    private LocalDateTime pickupTime;
    private LocalDateTime returnTime;
    private BigDecimal totalCost;
}
