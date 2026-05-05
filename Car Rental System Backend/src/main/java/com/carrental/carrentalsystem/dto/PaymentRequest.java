package com.carrental.carrentalsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    @NotNull
    private Long rentalId;
}
