package com.carrental.carrentalsystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange notificationExchange(@Value("${app.rabbitmq.exchange}") String exchangeName) {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue notificationQueue(@Value("${app.rabbitmq.queue}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Queue bookingQueue(@Value("${app.rabbitmq.booking.queue}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue,
            DirectExchange notificationExchange,
            @Value("${app.rabbitmq.routing-key}") String routingKey) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(routingKey);
    }

    @Bean
    public Binding bookingBinding(Queue bookingQueue,
            DirectExchange notificationExchange,
            @Value("${app.rabbitmq.booking.routing-key}") String routingKey) {
        return BindingBuilder.bind(bookingQueue).to(notificationExchange).with(routingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
