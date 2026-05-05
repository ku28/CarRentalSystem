package com.carrental.carrentalsystem.controller;

import com.carrental.carrentalsystem.dto.PaymentRequest;
import com.carrental.carrentalsystem.dto.PaymentResponse;
import com.carrental.carrentalsystem.dto.PaymentStatusUpdateRequest;
import com.carrental.carrentalsystem.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentResponse simulatePayment(@Valid @RequestBody PaymentRequest request) {
        return paymentService.simulatePayment(request.getRentalId());
    }

    @PatchMapping("/{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentResponse updateStatus(@PathVariable Long paymentId,
                                        @Valid @RequestBody PaymentStatusUpdateRequest request) {
        return paymentService.updateStatus(paymentId, request.getStatus());
    }

    @GetMapping("/rental/{rentalId}")
    public PaymentResponse getPaymentByRentalId(@PathVariable Long rentalId) {
        return paymentService.getByRentalId(rentalId);
    }
}
