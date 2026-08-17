package com.cogitolab.notificationhub.application.usecase;

import com.cogitolab.notificationhub.application.dto.NotificationDTO;
import com.cogitolab.notificationhub.application.dto.SensorEventDTO;
import com.cogitolab.notificationhub.application.service.RulesEngine;
import com.cogitolab.notificationhub.domain.model.Notification;
import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.port.NotificationRepositoryPort;
import com.cogitolab.notificationhub.domain.port.SensorEventRepositoryPort;
import com.cogitolab.notificationhub.domain.rules.RuleEvaluationResult;
import com.cogitolab.notificationhub.infrastructure.adapter.out.messaging.RabbitMQNotificationProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngestSensorEventUseCase {

    private static final Logger log = LoggerFactory.getLogger(IngestSensorEventUseCase.class);

    private final SensorEventRepositoryPort eventRepository;
    private final NotificationRepositoryPort notificationRepository;
    private final RulesEngine rulesEngine;
    private final RabbitMQNotificationProducer messagingProducer;
    private final String producerPhone;

    public IngestSensorEventUseCase(SensorEventRepositoryPort eventRepository,
                                  NotificationRepositoryPort notificationRepository,
                                  RulesEngine rulesEngine,
                                  RabbitMQNotificationProducer messagingProducer,
                                  @Value("${app.producer.phone:+5535999999999}") String producerPhone) {
        this.eventRepository = eventRepository;
        this.notificationRepository = notificationRepository;
        this.rulesEngine = rulesEngine;
        this.messagingProducer = messagingProducer;
        this.producerPhone = producerPhone;
    }

    public SensorEvent execute(SensorEventDTO dto) {
        log.info("[UseCase] Ingesting event: {}", dto.eventId());
        SensorEvent event = dto.toDomain();

        // 1. Idempotency & Persistence
        SensorEvent savedEvent = eventRepository.save(event);

        // 2. Publish Ingested Event Message
        try {
            messagingProducer.publishIngestedEvent(SensorEventDTO.fromDomain(savedEvent));
        } catch (Exception e) {
            log.warn("[UseCase] Messaging system unavailable for event publishing: {}", e.getMessage());
        }

        // 3. Evaluate Business Rules
        Optional<RuleEvaluationResult> resultOpt = rulesEngine.evaluate(savedEvent);
        if (resultOpt.isPresent()) {
            RuleEvaluationResult result = resultOpt.get();
            log.info("[UseCase] Rule triggered for event {}: {}", savedEvent.getEventId(), result.message());

            Notification notification = new Notification(
                savedEvent.getEventId(),
                savedEvent.getFarmId(),
                savedEvent.getDeviceId(),
                result.severity(),
                result.message(),
                producerPhone
            );

            Notification savedNotification = notificationRepository.save(notification);

            try {
                messagingProducer.publishNotification(NotificationDTO.fromDomain(savedNotification));
            } catch (Exception e) {
                log.warn("[UseCase] Messaging system unavailable for notification publishing: {}", e.getMessage());
            }
        } else {
            log.info("[UseCase] Event {} is normal. No notification triggered.", savedEvent.getEventId());
        }

        return savedEvent;
    }
}
