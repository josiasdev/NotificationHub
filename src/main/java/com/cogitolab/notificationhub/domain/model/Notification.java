package com.cogitolab.notificationhub.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Notification {
    private final String id;
    private final String eventId;
    private final String farmId;
    private final String deviceId;
    private final NotificationSeverity severity;
    private final String message;
    private final String recipientPhone;
    private NotificationStatus status;
    private int retryCount;
    private final OffsetDateTime createdAt;
    private OffsetDateTime sentAt;
    private String lastErrorMessage;

    public Notification(String eventId, String farmId, String deviceId,
                        NotificationSeverity severity, String message, String recipientPhone) {
        this(UUID.randomUUID().toString(), eventId, farmId, deviceId, severity, message, recipientPhone,
                NotificationStatus.PENDING, 0, OffsetDateTime.now(), null, null);
    }

    public Notification(String id, String eventId, String farmId, String deviceId,
                        NotificationSeverity severity, String message, String recipientPhone,
                        NotificationStatus status, int retryCount, OffsetDateTime createdAt,
                        OffsetDateTime sentAt, String lastErrorMessage) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.eventId = eventId;
        this.farmId = farmId;
        this.deviceId = deviceId;
        this.severity = severity;
        this.message = message;
        this.recipientPhone = recipientPhone;
        this.status = status != null ? status : NotificationStatus.PENDING;
        this.retryCount = retryCount;
        this.createdAt = createdAt != null ? createdAt : OffsetDateTime.now();
        this.sentAt = sentAt;
        this.lastErrorMessage = lastErrorMessage;
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = OffsetDateTime.now();
    }

    public void markAsFailed(String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.retryCount++;
        this.lastErrorMessage = errorMessage;
    }

    public void routeToDlq() {
        this.status = NotificationStatus.DLQ_ROUTED;
    }

    public String getId() { return id; }
    public String getEventId() { return eventId; }
    public String getFarmId() { return farmId; }
    public String getDeviceId() { return deviceId; }
    public NotificationSeverity getSeverity() { return severity; }
    public String getMessage() { return message; }
    public String getRecipientPhone() { return recipientPhone; }
    public NotificationStatus getStatus() { return status; }
    public int getRetryCount() { return retryCount; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getSentAt() { return sentAt; }
    public String getLastErrorMessage() { return lastErrorMessage; }
}
