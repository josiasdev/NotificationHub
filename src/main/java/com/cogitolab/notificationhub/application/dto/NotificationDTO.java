package com.cogitolab.notificationhub.application.dto;

import com.cogitolab.notificationhub.domain.model.Notification;
import com.cogitolab.notificationhub.domain.model.NotificationSeverity;
import com.cogitolab.notificationhub.domain.model.NotificationStatus;

import java.time.OffsetDateTime;

public record NotificationDTO(
    String id,
    String eventId,
    String farmId,
    String deviceId,
    NotificationSeverity severity,
    String message,
    String recipientPhone,
    NotificationStatus status,
    int retryCount,
    OffsetDateTime createdAt,
    OffsetDateTime sentAt,
    String lastErrorMessage
) {
    public static NotificationDTO fromDomain(Notification notification) {
        return new NotificationDTO(
            notification.getId(),
            notification.getEventId(),
            notification.getFarmId(),
            notification.getDeviceId(),
            notification.getSeverity(),
            notification.getMessage(),
            notification.getRecipientPhone(),
            notification.getStatus(),
            notification.getRetryCount(),
            notification.getCreatedAt(),
            notification.getSentAt(),
            notification.getLastErrorMessage()
        );
    }
}
