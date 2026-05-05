package com.carrental.carrentalsystem.messaging;

import com.carrental.carrentalsystem.entity.Notification;
import com.carrental.carrentalsystem.repository.NotificationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;

    @RabbitListener(
            queues = "${app.rabbitmq.queue}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consume(NotificationMessage message) {
        log.info("Notification received for user {}: {}", message.getUserId(), message.getMessage());
        notificationRepository.save(Notification.builder()
                .userId(message.getUserId())
                .message(message.getMessage())
                .createdAt(LocalDateTime.now())
                .build());
    }
}
