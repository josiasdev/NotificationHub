package com.cogitolab.notificationhub.infrastructure.adapter.out.messaging;

import com.cogitolab.notificationhub.application.dto.NotificationDTO;
import com.cogitolab.notificationhub.application.dto.SensorEventDTO;
import com.cogitolab.notificationhub.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQNotificationProducer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQNotificationProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQNotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishIngestedEvent(SensorEventDTO eventDTO) {
        log.info("[Producer] Publishing ingested event to RabbitMQ: {}", eventDTO.eventId());
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EVENTS_EXCHANGE,
            RabbitMQConfig.EVENTS_ROUTING_KEY,
            eventDTO
        );
    }

    public void publishNotification(NotificationDTO notificationDTO) {
        log.info("[Producer] Publishing notification to RabbitMQ for sending: {}", notificationDTO.id());
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.NOTIFICATIONS_EXCHANGE,
            RabbitMQConfig.NOTIFICATIONS_ROUTING_KEY,
            notificationDTO
        );
    }
}
