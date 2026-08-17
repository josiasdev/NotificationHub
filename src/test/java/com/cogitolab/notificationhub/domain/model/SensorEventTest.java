package com.cogitolab.notificationhub.domain.model;

import com.cogitolab.notificationhub.domain.exception.InvalidEventException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SensorEventTest {

    @Test
    @DisplayName("Should create valid AIR_TEMPERATURE SensorEvent successfully")
    void shouldCreateValidTemperatureEvent() {
        SensorEvent event = new SensorEvent(
            "evt-101",
            "farm-001",
            "sensor-temp-01",
            SensorType.AIR_TEMPERATURE,
            new BigDecimal("38.5"),
            null,
            "C",
            OffsetDateTime.now()
        );

        assertEquals("evt-101", event.getEventId());
        assertEquals(SensorType.AIR_TEMPERATURE, event.getType());
        assertEquals(new BigDecimal("38.5"), event.getNumericValue());
        assertNull(event.getTextValue());
    }

    @Test
    @DisplayName("Should throw InvalidEventException when eventId is blank")
    void shouldThrowWhenEventIdIsBlank() {
        InvalidEventException ex = assertThrows(InvalidEventException.class, () ->
            new SensorEvent(
                " ",
                "farm-001",
                "sensor-temp-01",
                SensorType.AIR_TEMPERATURE,
                new BigDecimal("38.5"),
                null,
                "C",
                OffsetDateTime.now()
            )
        );
        assertTrue(ex.getMessage().contains("eventId cannot be null or blank"));
    }

    @Test
    @DisplayName("Should throw InvalidEventException when percentage value is above 100%")
    void shouldThrowWhenHumidityIsAbove100Percent() {
        InvalidEventException ex = assertThrows(InvalidEventException.class, () ->
            new SensorEvent(
                "evt-102",
                "farm-001",
                "sensor-hum-01",
                SensorType.AIR_HUMIDITY,
                new BigDecimal("130.0"),
                null,
                "%",
                OffsetDateTime.now()
            )
        );
        assertTrue(ex.getMessage().contains("must be between 0% and 100%"));
    }

    @Test
    @DisplayName("Should throw InvalidEventException when EQUIPMENT_STATUS does not provide textValue")
    void shouldThrowWhenEquipmentStatusHasNoTextValue() {
        InvalidEventException ex = assertThrows(InvalidEventException.class, () ->
            new SensorEvent(
                "evt-103",
                "farm-001",
                "pump-01",
                SensorType.EQUIPMENT_STATUS,
                null,
                null,
                null,
                OffsetDateTime.now()
            )
        );
        assertTrue(ex.getMessage().contains("EQUIPMENT_STATUS must provide a text value"));
    }
}
