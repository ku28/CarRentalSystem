package com.carrental.carrentalsystem.dto;

import com.carrental.carrentalsystem.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatusUpdateRequest {

    @NotNull
    private PaymentStatus status;
}
