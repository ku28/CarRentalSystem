package com.carrental.carrentalsystem.messaging;

import com.carrental.carrentalsystem.entity.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.booking.routing-key}")
    private String routingKey;

    public void sendBookingCreated(Booking booking) {
        BookingMessageDTO message = BookingMessageDTO.builder()
                .bookingId(booking.getId())
                .userId(booking.getUser().getId())
                .carId(booking.getCar().getId())
                .message("Booking created successfully")
                .build();

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
