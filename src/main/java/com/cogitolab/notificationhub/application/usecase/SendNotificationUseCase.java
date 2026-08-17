package com.cogitolab.notificationhub.application.usecase;

import com.cogitolab.notificationhub.domain.model.Notification;
import com.cogitolab.notificationhub.domain.port.NotificationRepositoryPort;
import com.cogitolab.notificationhub.domain.port.NotificationSenderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendNotificationUseCase {

    private static final Logger log = LoggerFactory.getLogger(SendNotificationUseCase.class);

    private final NotificationRepositoryPort notificationRepository;
    private final NotificationSenderPort notificationSender;

    public SendNotificationUseCase(NotificationRepositoryPort notificationRepository,
                                  NotificationSenderPort notificationSender) {
        this.notificationRepository = notificationRepository;
        this.notificationSender = notificationSender;
    }

    public void execute(String notificationId) {
        log.info("[UseCase] Executing notification delivery for ID: {}", notificationId);
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + notificationId));

        notificationSender.send(notification);
    }
}
