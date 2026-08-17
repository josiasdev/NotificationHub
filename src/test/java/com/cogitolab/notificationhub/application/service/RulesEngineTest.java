package com.cogitolab.notificationhub.application.service;

import com.cogitolab.notificationhub.application.service.impl.*;
import com.cogitolab.notificationhub.domain.model.NotificationSeverity;
import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.model.SensorType;
import com.cogitolab.notificationhub.domain.rules.RuleEvaluationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RulesEngineTest {

    private RulesEngine rulesEngine;

    @BeforeEach
    void setUp() {
        rulesEngine = new RulesEngine(List.of(
            new AirTemperatureRule(),
            new AirHumidityRule(),
            new SoilMoistureRule(),
            new WaterReservoirRule(),
            new SiloLevelRule(),
            new EquipmentStatusRule()
        ));
    }

    @Test
    @DisplayName("AIR_TEMPERATURE > 35 °C should trigger temperature alert")
    void shouldTriggerTemperatureAlert() {
        SensorEvent event = new SensorEvent(
            "evt-001", "farm-001", "sensor-temp-01",
            SensorType.AIR_TEMPERATURE, new BigDecimal("38.5"), null, "C", OffsetDateTime.now()
        );

        Optional<RuleEvaluationResult> result = rulesEngine.evaluate(event);

        assertTrue(result.isPresent());
        assertTrue(result.get().triggered());
        assertEquals(NotificationSeverity.WARNING, result.get().severity());
        assertTrue(result.get().message().contains("38.5 °C"));
    }

    @Test
    @DisplayName("AIR_TEMPERATURE <= 35 °C should NOT trigger alert")
    void shouldNotTriggerNormalTemperature() {
        SensorEvent event = new SensorEvent(
            "evt-007", "farm-001", "sensor-temp-01",
            SensorType.AIR_TEMPERATURE, new BigDecimal("27.0"), null, "C", OffsetDateTime.now()
        );

        Optional<RuleEvaluationResult> result = rulesEngine.evaluate(event);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("AIR_HUMIDITY < 30% should trigger humidity alert")
    void shouldTriggerHumidityAlert() {
        SensorEvent event = new SensorEvent(
            "evt-002", "farm-001", "sensor-hum-01",
            SensorType.AIR_HUMIDITY, new BigDecimal("24.0"), null, "%", OffsetDateTime.now()
        );

        Optional<RuleEvaluationResult> result = rulesEngine.evaluate(event);

        assertTrue(result.isPresent());
        assertTrue(result.get().triggered());
        assertTrue(result.get().message().contains("24.0%"));
    }

    @Test
    @DisplayName("SOIL_MOISTURE < 20% should trigger irrigation alert")
    void shouldTriggerSoilMoistureAlert() {
        SensorEvent event = new SensorEvent(
            "evt-003", "farm-001", "sensor-soil-01",
            SensorType.SOIL_MOISTURE, new BigDecimal("17.0"), null, "%", OffsetDateTime.now()
        );

        Optional<RuleEvaluationResult> result = rulesEngine.evaluate(event);

        assertTrue(result.isPresent());
        assertTrue(result.get().triggered());
        assertTrue(result.get().message().contains("17.0%"));
    }

    @Test
    @DisplayName("WATER_RESERVOIR_LEVEL < 15% should trigger reservoir alert")
    void shouldTriggerReservoirAlert() {
        SensorEvent event = new SensorEvent(
            "evt-004", "farm-001", "reservoir-01",
            SensorType.WATER_RESERVOIR_LEVEL, new BigDecimal("12.0"), null, "%", OffsetDateTime.now()
        );

        Optional<RuleEvaluationResult> result = rulesEngine.evaluate(event);

        assertTrue(result.isPresent());
        assertTrue(result.get().triggered());
        assertTrue(result.get().message().contains("12.0%"));
    }

    @Test
    @DisplayName("SILO_LEVEL < 15% should trigger silo alert")
    void shouldTriggerSiloAlert() {
        SensorEvent event = new SensorEvent(
            "evt-005", "farm-001", "silo-01",
            SensorType.SILO_LEVEL, new BigDecimal("10.0"), null, "%", OffsetDateTime.now()
        );

        Optional<RuleEvaluationResult> result = rulesEngine.evaluate(event);

        assertTrue(result.isPresent());
        assertTrue(result.get().triggered());
        assertTrue(result.get().message().contains("10.0%"));
    }

    @Test
    @DisplayName("EQUIPMENT_STATUS = FAILURE should trigger critical equipment failure alert")
    void shouldTriggerEquipmentFailureAlert() {
        SensorEvent event = new SensorEvent(
            "evt-006", "farm-001", "irrigation-pump-01",
            SensorType.EQUIPMENT_STATUS, null, "FAILURE", null, OffsetDateTime.now()
        );

        Optional<RuleEvaluationResult> result = rulesEngine.evaluate(event);

        assertTrue(result.isPresent());
        assertTrue(result.get().triggered());
        assertEquals(NotificationSeverity.CRITICAL, result.get().severity());
        assertTrue(result.get().message().contains("irrigation-pump-01"));
    }
}
