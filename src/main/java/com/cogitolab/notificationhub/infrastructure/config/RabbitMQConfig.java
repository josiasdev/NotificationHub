package com.cogitolab.notificationhub.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EVENTS_EXCHANGE = "iot.events.exchange";
    public static final String EVENTS_QUEUE = "iot.events.queue";
    public static final String EVENTS_ROUTING_KEY = "iot.event.ingested";

    public static final String NOTIFICATIONS_EXCHANGE = "iot.notifications.exchange";
    public static final String NOTIFICATIONS_QUEUE = "iot.notifications.queue";
    public static final String NOTIFICATIONS_ROUTING_KEY = "iot.notification.process";

    public static final String NOTIFICATIONS_DLQ_EXCHANGE = "iot.notifications.dlq.exchange";
    public static final String NOTIFICATIONS_DLQ = "iot.notifications.dlq";
    public static final String NOTIFICATIONS_DLQ_ROUTING_KEY = "iot.notification.dlq";

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE);
    }

    @Bean
    public Queue eventsQueue() {
        return QueueBuilder.durable(EVENTS_QUEUE).build();
    }

    @Bean
    public Binding eventsBinding(Queue eventsQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(eventsQueue).to(eventsExchange).with(EVENTS_ROUTING_KEY);
    }

    @Bean
    public TopicExchange notificationsExchange() {
        return new TopicExchange(NOTIFICATIONS_EXCHANGE);
    }

    @Bean
    public TopicExchange notificationsDlqExchange() {
        return new TopicExchange(NOTIFICATIONS_DLQ_EXCHANGE);
    }

    @Bean
    public Queue notificationsDlq() {
        return QueueBuilder.durable(NOTIFICATIONS_DLQ).build();
    }

    @Bean
    public Binding notificationsDlqBinding(Queue notificationsDlq, TopicExchange notificationsDlqExchange) {
        return BindingBuilder.bind(notificationsDlq).to(notificationsDlqExchange).with(NOTIFICATIONS_DLQ_ROUTING_KEY);
    }

    @Bean
    public Queue notificationsQueue() {
        return QueueBuilder.durable(NOTIFICATIONS_QUEUE)
            .withArgument("x-dead-letter-exchange", NOTIFICATIONS_DLQ_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", NOTIFICATIONS_DLQ_ROUTING_KEY)
            .build();
    }

    @Bean
    public Binding notificationsBinding(Queue notificationsQueue, TopicExchange notificationsExchange) {
        return BindingBuilder.bind(notificationsQueue).to(notificationsExchange).with(NOTIFICATIONS_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
