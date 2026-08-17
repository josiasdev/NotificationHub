package com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.document;

import com.cogitolab.notificationhub.domain.model.Notification;
import com.cogitolab.notificationhub.domain.model.NotificationSeverity;
import com.cogitolab.notificationhub.domain.model.NotificationStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Document(collection = "notifications")
public class NotificationDocument {

    @Id
    private String id;
    private String eventId;
    private String farmId;
    private String deviceId;
    private NotificationSeverity severity;
    private String message;
    private String recipientPhone;
    private NotificationStatus status;
    private int retryCount;
    private OffsetDateTime createdAt;
    private OffsetDateTime sentAt;
    private String lastErrorMessage;

    public NotificationDocument() {}

    public static NotificationDocument fromDomain(Notification notification) {
        NotificationDocument doc = new NotificationDocument();
        doc.id = notification.getId();
        doc.eventId = notification.getEventId();
        doc.farmId = notification.getFarmId();
        doc.deviceId = notification.getDeviceId();
        doc.severity = notification.getSeverity();
        doc.message = notification.getMessage();
        doc.recipientPhone = notification.getRecipientPhone();
        doc.status = notification.getStatus();
        doc.retryCount = notification.getRetryCount();
        doc.createdAt = notification.getCreatedAt();
        doc.sentAt = notification.getSentAt();
        doc.lastErrorMessage = notification.getLastErrorMessage();
        return doc;
    }

    public Notification toDomain() {
        return new Notification(
            id, eventId, farmId, deviceId, severity, message, recipientPhone,
            status, retryCount, createdAt, sentAt, lastErrorMessage
        );
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getFarmId() { return farmId; }
    public void setFarmId(String farmId) { this.farmId = farmId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public NotificationSeverity getSeverity() { return severity; }
    public void setSeverity(NotificationSeverity severity) { this.severity = severity; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }
    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getSentAt() { return sentAt; }
    public void setSentAt(OffsetDateTime sentAt) { this.sentAt = sentAt; }
    public String getLastErrorMessage() { return lastErrorMessage; }
    public void setLastErrorMessage(String lastErrorMessage) { this.lastErrorMessage = lastErrorMessage; }
}
