package com.cogitolab.notificationhub.application.dto;

import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.model.SensorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SensorEventDTO(
    @NotBlank(message = "eventId is required")
    String eventId,

    @NotBlank(message = "farmId is required")
    String farmId,

    @NotBlank(message = "deviceId is required")
    String deviceId,

    @NotNull(message = "type is required")
    SensorType type,

    Object value,

    String unit,

    @NotNull(message = "timestamp is required")
    OffsetDateTime timestamp
) {
    public SensorEvent toDomain() {
        BigDecimal numericVal = null;
        String textVal = null;

        if (value != null) {
            if (value instanceof Number num) {
                numericVal = BigDecimal.valueOf(num.doubleValue());
            } else {
                String strVal = value.toString().trim();
                try {
                    numericVal = new BigDecimal(strVal);
                } catch (NumberFormatException e) {
                    textVal = strVal;
                }
            }
        }

        return new SensorEvent(eventId, farmId, deviceId, type, numericVal, textVal, unit, timestamp);
    }

    public static SensorEventDTO fromDomain(SensorEvent event) {
        return new SensorEventDTO(
            event.getEventId(),
            event.getFarmId(),
            event.getDeviceId(),
            event.getType(),
            event.getValue(),
            event.getUnit(),
            event.getTimestamp()
        );
    }
}
