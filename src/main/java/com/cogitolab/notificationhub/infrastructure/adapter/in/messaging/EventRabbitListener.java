package com.cogitolab.notificationhub.infrastructure.adapter.in.messaging;

import com.cogitolab.notificationhub.application.dto.NotificationDTO;
import com.cogitolab.notificationhub.application.usecase.SendNotificationUseCase;
import com.cogitolab.notificationhub.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventRabbitListener {

    private static final Logger log = LoggerFactory.getLogger(EventRabbitListener.class);

    private final SendNotificationUseCase sendNotificationUseCase;

    public EventRabbitListener(SendNotificationUseCase sendNotificationUseCase) {
        this.sendNotificationUseCase = sendNotificationUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATIONS_QUEUE)
    public void processNotification(NotificationDTO notificationDTO) {
        log.info("[RabbitListener] Received notification message for ID: {}", notificationDTO.id());
        sendNotificationUseCase.execute(notificationDTO.id());
    }
}
