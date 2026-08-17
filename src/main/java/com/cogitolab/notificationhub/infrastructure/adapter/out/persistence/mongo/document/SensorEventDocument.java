package com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.document;

import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.model.SensorType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Document(collection = "sensor_events")
public class SensorEventDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String eventId;

    private String farmId;
    private String deviceId;
    private SensorType type;
    private BigDecimal numericValue;
    private String textValue;
    private String unit;
    private OffsetDateTime timestamp;
    private OffsetDateTime ingestedAt;

    public SensorEventDocument() {}

    public SensorEventDocument(String eventId, String farmId, String deviceId, SensorType type,
                               BigDecimal numericValue, String textValue, String unit,
                               OffsetDateTime timestamp, OffsetDateTime ingestedAt) {
        this.eventId = eventId;
        this.farmId = farmId;
        this.deviceId = deviceId;
        this.type = type;
        this.numericValue = numericValue;
        this.textValue = textValue;
        this.unit = unit;
        this.timestamp = timestamp;
        this.ingestedAt = ingestedAt;
    }

    public static SensorEventDocument fromDomain(SensorEvent event) {
        return new SensorEventDocument(
            event.getEventId(),
            event.getFarmId(),
            event.getDeviceId(),
            event.getType(),
            event.getNumericValue(),
            event.getTextValue(),
            event.getUnit(),
            event.getTimestamp(),
            event.getIngestedAt()
        );
    }

    public SensorEvent toDomain() {
        return new SensorEvent(
            eventId,
            farmId,
            deviceId,
            type,
            numericValue,
            textValue,
            unit,
            timestamp,
            ingestedAt
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
    public SensorType getType() { return type; }
    public void setType(SensorType type) { this.type = type; }
    public BigDecimal getNumericValue() { return numericValue; }
    public void setNumericValue(BigDecimal numericValue) { this.numericValue = numericValue; }
    public String getTextValue() { return textValue; }
    public void setTextValue(String textValue) { this.textValue = textValue; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
    public OffsetDateTime getIngestedAt() { return ingestedAt; }
    public void setIngestedAt(OffsetDateTime ingestedAt) { this.ingestedAt = ingestedAt; }
}
