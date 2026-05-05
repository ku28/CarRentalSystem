package com.carrental.carrentalsystem.dto;

import com.carrental.carrentalsystem.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {

    private Long id;
    private Long rentalId;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
}
