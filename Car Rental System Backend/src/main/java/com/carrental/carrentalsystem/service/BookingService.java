package com.carrental.carrentalsystem.service;

import com.carrental.carrentalsystem.dto.BookingRequest;
import com.carrental.carrentalsystem.dto.BookingResponse;
import com.carrental.carrentalsystem.entity.Booking;
import com.carrental.carrentalsystem.entity.Car;
import com.carrental.carrentalsystem.entity.User;
import com.carrental.carrentalsystem.enums.AvailabilityStatus;
import com.carrental.carrentalsystem.enums.BookingStatus;
import com.carrental.carrentalsystem.exception.BadRequestException;
import com.carrental.carrentalsystem.exception.ResourceNotFoundException;
import com.carrental.carrentalsystem.messaging.BookingProducer;
import com.carrental.carrentalsystem.messaging.NotificationProducer;
import com.carrental.carrentalsystem.repository.BookingRepository;
import com.carrental.carrentalsystem.util.EntityDtoMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CarService carService;
    private final UserService userService;
    private final NotificationProducer notificationProducer;
    private final BookingProducer bookingProducer;
    private final EmailService emailService;

    @Transactional
    public BookingResponse createBooking(Long userId, BookingRequest request) {
        validateDates(request);
        User user = userService.findEntityById(userId);
        Car car = carService.findEntityById(request.getCarId());

        if (car.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE) {
            throw new BadRequestException("Car is not currently available for booking");
        }

        Booking booking = bookingRepository.save(Booking.builder()
                .user(user)
                .car(car)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(BookingStatus.CREATED)
                .build());

        car.setAvailabilityStatus(AvailabilityStatus.BOOKED);
        carService.save(car);

        bookingProducer.sendBookingCreated(booking);
        notificationProducer.send(userId, "Booking created successfully for car " + car.getBrand() + " " + car.getModel());
        
        String emailBody = "Dear " + user.getName() + ",\n\nYour booking for the car " + car.getBrand() + " " + car.getModel() + " from " + request.getStartDate() + " to " + request.getEndDate() + " has been confirmed.";
        emailService.sendEmail(user.getEmail(), "Booking Confirmation", emailBody);

        return EntityDtoMapper.toBookingResponse(booking);
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId, Long userId) {
        Booking booking = findEntityById(bookingId);
        if (!booking.getUser().getId().equals(userId)) {
            throw new BadRequestException("You can cancel only your own bookings");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BadRequestException("Booking cannot be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.getCar().setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        carService.save(booking.getCar());
        return EntityDtoMapper.toBookingResponse(bookingRepository.save(booking));
    }

    public List<BookingResponse> getBookingsForUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(EntityDtoMapper::toBookingResponse)
                .toList();
    }

    public Booking findEntityById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
    }

    public List<BookingResponse> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status).stream()
                .map(EntityDtoMapper::toBookingResponse)
                .toList();
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(EntityDtoMapper::toBookingResponse)
                .toList();
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    private void validateDates(BookingRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date must be on or after start date");
        }
    }
}
