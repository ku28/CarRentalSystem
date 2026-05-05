package com.carrental.carrentalsystem.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingConsumer {

    @RabbitListener(
            queues = "${app.rabbitmq.booking.queue}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consumeBookingCreated(BookingMessageDTO bookingMessage) {
        log.info(
                "Booking message received: bookingId={}, userId={}, carId={}, message={}",
                bookingMessage.getBookingId(),
                bookingMessage.getUserId(),
                bookingMessage.getCarId(),
                bookingMessage.getMessage()
        );
    }
}
