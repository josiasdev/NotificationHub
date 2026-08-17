package com.cogitolab.notificationhub.domain.port;

import com.cogitolab.notificationhub.domain.model.SensorEvent;

import java.util.List;
import java.util.Optional;

public interface SensorEventRepositoryPort {
    SensorEvent save(SensorEvent event);
    boolean existsByEventId(String eventId);
    Optional<SensorEvent> findByEventId(String eventId);
    List<SensorEvent> findAll();
}
