package com.carrental.carrentalsystem.controller;

import com.carrental.carrentalsystem.dto.RentalResponse;
import com.carrental.carrentalsystem.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/pickup/{bookingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public RentalResponse pickupCar(@PathVariable Long bookingId) {
        return rentalService.pickupCar(bookingId);
    }

    @PostMapping("/return/{rentalId}")
    @PreAuthorize("hasRole('ADMIN')")
    public RentalResponse returnCar(@PathVariable Long rentalId) {
        return rentalService.returnCar(rentalId);
    }

    @GetMapping("/booking/{bookingId}")
    public RentalResponse getRentalByBookingId(@PathVariable Long bookingId) {
        return rentalService.getRentalByBookingId(bookingId);
    }
}
