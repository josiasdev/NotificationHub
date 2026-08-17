package com.cogitolab.notificationhub.domain.model;

import com.cogitolab.notificationhub.domain.exception.InvalidEventException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

public class SensorEvent {
    private final String eventId;
    private final String farmId;
    private final String deviceId;
    private final SensorType type;
    private final BigDecimal numericValue;
    private final String textValue;
    private final String unit;
    private final OffsetDateTime timestamp;
    private final OffsetDateTime ingestedAt;

    public SensorEvent(String eventId, String farmId, String deviceId, SensorType type,
                       BigDecimal numericValue, String textValue, String unit,
                       OffsetDateTime timestamp) {
        this(eventId, farmId, deviceId, type, numericValue, textValue, unit, timestamp, OffsetDateTime.now());
    }

    public SensorEvent(String eventId, String farmId, String deviceId, SensorType type,
                       BigDecimal numericValue, String textValue, String unit,
                       OffsetDateTime timestamp, OffsetDateTime ingestedAt) {
        this.eventId = validateNotNull(eventId, "eventId cannot be null or blank");
        this.farmId = validateNotNull(farmId, "farmId cannot be null or blank");
        this.deviceId = validateNotNull(deviceId, "deviceId cannot be null or blank");
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.numericValue = numericValue;
        this.textValue = textValue;
        this.unit = unit;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
        this.ingestedAt = ingestedAt != null ? ingestedAt : OffsetDateTime.now();

        validateDomainRules();
    }

    private String validateNotNull(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidEventException(message);
        }
        return value;
    }

    private void validateDomainRules() {
        if (type == SensorType.EQUIPMENT_STATUS) {
            if (textValue == null || textValue.trim().isEmpty()) {
                throw new InvalidEventException("EQUIPMENT_STATUS must provide a text value (e.g., FAILURE, OPERATIONAL)");
            }
        } else {
            if (numericValue == null) {
                throw new InvalidEventException("Numeric sensor type " + type + " must provide a numeric value");
            }
            if ((type == SensorType.AIR_HUMIDITY || type == SensorType.SOIL_MOISTURE 
                 || type == SensorType.WATER_RESERVOIR_LEVEL || type == SensorType.SILO_LEVEL)
                && (numericValue.compareTo(BigDecimal.ZERO) < 0 || numericValue.compareTo(BigDecimal.valueOf(100)) > 0)) {
                throw new InvalidEventException("Percentage value for " + type + " must be between 0% and 100%. Received: " + numericValue);
            }
        }
    }

    public String getEventId() { return eventId; }
    public String getFarmId() { return farmId; }
    public String getDeviceId() { return deviceId; }
    public SensorType getType() { return type; }
    public BigDecimal getNumericValue() { return numericValue; }
    public String getTextValue() { return textValue; }
    public String getUnit() { return unit; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public OffsetDateTime getIngestedAt() { return ingestedAt; }

    public Object getValue() {
        return type == SensorType.EQUIPMENT_STATUS ? textValue : numericValue;
    }
}
