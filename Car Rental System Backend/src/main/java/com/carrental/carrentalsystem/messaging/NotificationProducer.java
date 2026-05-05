package com.carrental.carrentalsystem.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public void send(Long userId, String message) {
        NotificationMessage payload = NotificationMessage.builder()
                .userId(userId)
                .message(message)
                .build();

        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
    }
}
