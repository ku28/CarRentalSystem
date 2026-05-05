package com.carrental.carrentalsystem.service;

import com.carrental.carrentalsystem.dto.RentalResponse;
import com.carrental.carrentalsystem.entity.Booking;
import com.carrental.carrentalsystem.entity.Rental;
import com.carrental.carrentalsystem.enums.AvailabilityStatus;
import com.carrental.carrentalsystem.enums.BookingStatus;
import com.carrental.carrentalsystem.exception.BadRequestException;
import com.carrental.carrentalsystem.exception.ResourceNotFoundException;
import com.carrental.carrentalsystem.repository.RentalRepository;
import com.carrental.carrentalsystem.util.EntityDtoMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final BookingService bookingService;
    private final CarService carService;

    @Transactional
    public RentalResponse pickupCar(Long bookingId) {
        Booking booking = bookingService.findEntityById(bookingId);
        if (booking.getStatus() != BookingStatus.CREATED) {
            throw new BadRequestException("Only created bookings can be picked up");
        }
        rentalRepository.findByBookingId(bookingId).ifPresent(existing -> {
            throw new BadRequestException("Rental already exists for booking " + bookingId);
        });

        booking.setStatus(BookingStatus.PICKED_UP);
        booking.getCar().setAvailabilityStatus(AvailabilityStatus.RENTED);
        bookingService.save(booking);
        carService.save(booking.getCar());

        Rental rental = rentalRepository.save(Rental.builder()
                .booking(booking)
                .pickupTime(LocalDateTime.now())
                .build());
        return EntityDtoMapper.toRentalResponse(rental);
    }

    @Transactional
    public RentalResponse returnCar(Long rentalId) {
        Rental rental = findEntityById(rentalId);
        if (rental.getReturnTime() != null) {
            throw new BadRequestException("Car has already been returned");
        }

        rental.setReturnTime(LocalDateTime.now());
        rental.setTotalCost(calculateTotalCost(rental));

        Booking booking = rental.getBooking();
        booking.setStatus(BookingStatus.COMPLETED);
        booking.getCar().setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        bookingService.save(booking);
        carService.save(booking.getCar());

        return EntityDtoMapper.toRentalResponse(rentalRepository.save(rental));
    }

    public RentalResponse getRentalByBookingId(Long bookingId) {
        return rentalRepository.findByBookingId(bookingId)
                .map(EntityDtoMapper::toRentalResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found for booking id: " + bookingId));
    }

    public Rental findEntityById(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with id: " + rentalId));
    }

    public java.util.List<RentalResponse> getActiveRentals() {
        return rentalRepository.findByReturnTimeIsNull().stream()
                .map(EntityDtoMapper::toRentalResponse)
                .toList();
    }

    private BigDecimal calculateTotalCost(Rental rental) {
        LocalDateTime pickupTime = rental.getPickupTime();
        LocalDateTime returnTime = rental.getReturnTime();

        long hours = Math.max(1, Duration.between(pickupTime, returnTime).toHours());
        long days = (hours + 23) / 24;
        return rental.getBooking().getCar().getPricePerDay()
                .multiply(BigDecimal.valueOf(days))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
