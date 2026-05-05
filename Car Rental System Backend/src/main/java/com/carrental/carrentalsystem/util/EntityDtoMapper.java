package com.carrental.carrentalsystem.util;

import com.carrental.carrentalsystem.dto.BookingResponse;
import com.carrental.carrentalsystem.dto.CarResponse;
import com.carrental.carrentalsystem.dto.NotificationResponse;
import com.carrental.carrentalsystem.dto.PageResponse;
import com.carrental.carrentalsystem.dto.PaymentResponse;
import com.carrental.carrentalsystem.dto.RentalResponse;
import com.carrental.carrentalsystem.dto.UserResponse;
import com.carrental.carrentalsystem.entity.Booking;
import com.carrental.carrentalsystem.entity.Car;
import com.carrental.carrentalsystem.entity.Notification;
import com.carrental.carrentalsystem.entity.Payment;
import com.carrental.carrentalsystem.entity.Rental;
import com.carrental.carrentalsystem.entity.User;
import java.util.function.Function;
import org.springframework.data.domain.Page;

public final class EntityDtoMapper {

    private EntityDtoMapper() {
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public static CarResponse toCarResponse(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .category(car.getCategory())
                .pricePerDay(car.getPricePerDay())
                .availabilityStatus(car.getAvailabilityStatus())
                .build();
    }

    public static BookingResponse toBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .carId(booking.getCar().getId())
                .carName(booking.getCar().getBrand() + " " + booking.getCar().getModel())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();
    }

    public static RentalResponse toRentalResponse(Rental rental) {
        return RentalResponse.builder()
                .id(rental.getId())
                .bookingId(rental.getBooking().getId())
                .pickupTime(rental.getPickupTime())
                .returnTime(rental.getReturnTime())
                .totalCost(rental.getTotalCost())
                .build();
    }

    public static PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .rentalId(payment.getRental().getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

    public static NotificationResponse toNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper) {
        return PageResponse.<R>builder()
                .content(page.getContent().stream().map(mapper).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
