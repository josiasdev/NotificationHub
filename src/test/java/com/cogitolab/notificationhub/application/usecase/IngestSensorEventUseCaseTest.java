package com.cogitolab.notificationhub.application.usecase;

import com.cogitolab.notificationhub.application.dto.SensorEventDTO;
import com.cogitolab.notificationhub.application.service.RulesEngine;
import com.cogitolab.notificationhub.domain.exception.DuplicateEventException;
import com.cogitolab.notificationhub.domain.model.Notification;
import com.cogitolab.notificationhub.domain.model.NotificationSeverity;
import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.model.SensorType;
import com.cogitolab.notificationhub.domain.port.NotificationRepositoryPort;
import com.cogitolab.notificationhub.domain.port.SensorEventRepositoryPort;
import com.cogitolab.notificationhub.domain.rules.RuleEvaluationResult;
import com.cogitolab.notificationhub.infrastructure.adapter.out.messaging.RabbitMQNotificationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IngestSensorEventUseCaseTest {

    @Mock
    private SensorEventRepositoryPort eventRepository;

    @Mock
    private NotificationRepositoryPort notificationRepository;

    @Mock
    private RulesEngine rulesEngine;

    @Mock
    private RabbitMQNotificationProducer messagingProducer;

    private IngestSensorEventUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new IngestSensorEventUseCase(
            eventRepository,
            notificationRepository,
            rulesEngine,
            messagingProducer,
            "+5535999999999"
        );
    }

    @Test
    @DisplayName("Ingesting alert event should save event, create notification and publish to RabbitMQ")
    void shouldIngestAlertEvent() {
        SensorEventDTO dto = new SensorEventDTO(
            "evt-001", "farm-001", "sensor-temp-01",
            SensorType.AIR_TEMPERATURE, 38.5, "C", OffsetDateTime.now()
        );

        when(eventRepository.save(any(SensorEvent.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rulesEngine.evaluate(any(SensorEvent.class))).thenReturn(
            Optional.of(RuleEvaluationResult.alert(NotificationSeverity.WARNING, "High Temperature Alert"))
        );

        Notification savedNotification = new Notification(
            "evt-001", "farm-001", "sensor-temp-01",
            NotificationSeverity.WARNING, "High Temperature Alert", "+5535999999999"
        );
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        SensorEvent result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals("evt-001", result.getEventId());
        verify(eventRepository, times(1)).save(any(SensorEvent.class));
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(messagingProducer, times(1)).publishIngestedEvent(any());
        verify(messagingProducer, times(1)).publishNotification(any());
    }

    @Test
    @DisplayName("Ingesting duplicate event should throw DuplicateEventException")
    void shouldThrowOnDuplicateEvent() {
        SensorEventDTO dto = new SensorEventDTO(
            "evt-100", "farm-001", "sensor-temp-01",
            SensorType.AIR_TEMPERATURE, 39.0, "C", OffsetDateTime.now()
        );

        when(eventRepository.save(any(SensorEvent.class))).thenThrow(new DuplicateEventException("evt-100"));

        assertThrows(DuplicateEventException.class, () -> useCase.execute(dto));
        verify(notificationRepository, never()).save(any());
    }
}
