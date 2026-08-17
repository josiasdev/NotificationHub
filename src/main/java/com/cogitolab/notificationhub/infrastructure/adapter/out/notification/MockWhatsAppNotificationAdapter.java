package com.cogitolab.notificationhub.infrastructure.adapter.out.notification;

import com.cogitolab.notificationhub.domain.model.Notification;
import com.cogitolab.notificationhub.domain.port.NotificationRepositoryPort;
import com.cogitolab.notificationhub.domain.port.NotificationSenderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MockWhatsAppNotificationAdapter implements NotificationSenderPort {

    private static final Logger log = LoggerFactory.getLogger(MockWhatsAppNotificationAdapter.class);

    private final NotificationRepositoryPort notificationRepository;
    private final boolean simulateFailures;

    public MockWhatsAppNotificationAdapter(NotificationRepositoryPort notificationRepository,
                                           @Value("${app.notification.simulate-failures:false}") boolean simulateFailures) {
        this.notificationRepository = notificationRepository;
        this.simulateFailures = simulateFailures;
    }

    @Override
    public void send(Notification notification) {
        log.info("[MockWhatsAppProvider] Sending notification {} to {}: '{}'",
            notification.getId(), notification.getRecipientPhone(), notification.getMessage());

        if (simulateFailures && Math.random() < 0.5) {
            String errorMsg = "Simulated WhatsApp API failure (503 Service Unavailable)";
            notification.markAsFailed(errorMsg);
            notificationRepository.save(notification);
            log.warn("[MockWhatsAppProvider] Failed to send notification {}: {}", notification.getId(), errorMsg);
            throw new RuntimeException(errorMsg);
        }

        notification.markAsSent();
        notificationRepository.save(notification);
        log.info("[MockWhatsAppProvider] Notification {} sent successfully!", notification.getId());
    }
}
