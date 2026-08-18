package com.cogitolab.notificationhub.integration;

import com.cogitolab.notificationhub.application.dto.SensorEventDTO;
import com.cogitolab.notificationhub.domain.model.NotificationStatus;
import com.cogitolab.notificationhub.domain.model.SensorType;
import com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.document.NotificationDocument;
import com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.document.SensorEventDocument;
import com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.repository.SpringDataMongoEventRepository;
import com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.repository.SpringDataMongoNotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@EnabledIfEnvironmentVariable(named = "DOCKER_AVAILABLE", matches = "true")
class IngestSensorEventIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.13-management");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringDataMongoEventRepository eventRepository;

    @Autowired
    private SpringDataMongoNotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    @Test
    @DisplayName("Integration Test: End-to-end event ingestion, rule evaluation, MongoDB persistence and RabbitMQ delivery")
    void testEndToEndEventIngestionAndNotification() throws Exception {
        SensorEventDTO dto = new SensorEventDTO(
            "it-event-001",
            "farm-001",
            "sensor-temp-01",
            SensorType.AIR_TEMPERATURE,
            38.5,
            "C",
            OffsetDateTime.now()
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());

        // Verify event stored in MongoDB
        Optional<SensorEventDocument> eventDoc = eventRepository.findByEventId("it-event-001");
        assertTrue(eventDoc.isPresent());
        assertEquals("sensor-temp-01", eventDoc.get().getDeviceId());

        // Allow async RabbitMQ listener to process notification
        Thread.sleep(1000);

        // Verify notification generated and processed
        List<NotificationDocument> notifications = notificationRepository.findByEventId("it-event-001");
        assertFalse(notifications.isEmpty());
        NotificationDocument notification = notifications.get(0);
        assertTrue(notification.getMessage().contains("38.5 °C"));
        assertEquals(NotificationStatus.SENT, notification.getStatus());
    }

    @Test
    @DisplayName("Integration Test: Idempotency enforcement with duplicate eventId returns HTTP 409 Conflict")
    void testIdempotencyDuplicateEvent() throws Exception {
        SensorEventDTO dto = new SensorEventDTO(
            "it-event-duplicate",
            "farm-001",
            "sensor-hum-01",
            SensorType.AIR_HUMIDITY,
            24.0,
            "%",
            OffsetDateTime.now()
        );

        // First ingestion -> 201 Created
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());

        // Second ingestion with identical eventId -> 409 Conflict
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isConflict());

        // Verify only 1 event document stored
        assertEquals(1, eventRepository.findAll().size());
    }
}
