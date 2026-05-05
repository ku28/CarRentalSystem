package com.carrental.carrentalsystem.controller;

import com.carrental.carrentalsystem.dto.CarRequest;
import com.carrental.carrentalsystem.dto.CarResponse;
import com.carrental.carrentalsystem.dto.PageResponse;
import com.carrental.carrentalsystem.enums.AvailabilityStatus;
import com.carrental.carrentalsystem.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CarResponse addCar(@Valid @RequestBody CarRequest request) {
        return carService.addCar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CarResponse updateCar(@PathVariable Long id, @Valid @RequestBody CarRequest request) {
        return carService.updateCar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }

    @GetMapping
    public PageResponse<CarResponse> getAllCars(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return carService.getAllCars(page, size);
    }

    @GetMapping("/search")
    public PageResponse<CarResponse> searchCars(@RequestParam(required = false) String brand,
                                                @RequestParam(required = false) String model,
                                                @RequestParam(required = false) String category,
                                                @RequestParam(required = false) AvailabilityStatus availabilityStatus,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return carService.searchCars(brand, model, category, availabilityStatus, page, size);
    }
}
