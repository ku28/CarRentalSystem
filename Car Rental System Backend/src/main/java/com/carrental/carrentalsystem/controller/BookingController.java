package com.carrental.carrentalsystem.controller;

import com.carrental.carrentalsystem.dto.BookingRequest;
import com.carrental.carrentalsystem.dto.BookingResponse;
import com.carrental.carrentalsystem.dto.CarResponse;
import com.carrental.carrentalsystem.dto.PageResponse;
import com.carrental.carrentalsystem.enums.AvailabilityStatus;
import com.carrental.carrentalsystem.enums.BookingStatus;
import com.carrental.carrentalsystem.service.BookingService;
import com.carrental.carrentalsystem.service.CarService;
import com.carrental.carrentalsystem.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final CarService carService;

    @GetMapping("/search")
    public PageResponse<CarResponse> searchCars(@RequestParam(required = false) String brand,
                                                @RequestParam(required = false) String model,
                                                @RequestParam(required = false) String category,
                                                @RequestParam(defaultValue = "AVAILABLE") AvailabilityStatus availabilityStatus,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return carService.searchCars(brand, model, category, availabilityStatus, page, size);
    }

    @PostMapping
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request, Principal principal) {
        Long userId = userService.getUserByEmail(principal.getName()).getId();
        return bookingService.createBooking(userId, request);
    }

    @DeleteMapping("/{bookingId}")
    public BookingResponse cancelBooking(@PathVariable Long bookingId, Principal principal) {
        Long userId = userService.getUserByEmail(principal.getName()).getId();
        return bookingService.cancelBooking(bookingId, userId);
    }

    @GetMapping("/me")
    public List<BookingResponse> getMyBookings(Principal principal) {
        Long userId = userService.getUserByEmail(principal.getName()).getId();
        return bookingService.getBookingsForUser(userId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> getBookingsByStatus(@PathVariable BookingStatus status) {
        return bookingService.getBookingsByStatus(status);
    }
}

