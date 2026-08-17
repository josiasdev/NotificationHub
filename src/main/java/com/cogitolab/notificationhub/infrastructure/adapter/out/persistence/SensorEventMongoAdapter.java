package com.cogitolab.notificationhub.infrastructure.adapter.out.persistence;

import com.cogitolab.notificationhub.domain.exception.DuplicateEventException;
import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.port.SensorEventRepositoryPort;
import com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.document.SensorEventDocument;
import com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.repository.SpringDataMongoEventRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SensorEventMongoAdapter implements SensorEventRepositoryPort {

    private final SpringDataMongoEventRepository repository;

    public SensorEventMongoAdapter(SpringDataMongoEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public SensorEvent save(SensorEvent event) {
        if (existsByEventId(event.getEventId())) {
            throw new DuplicateEventException(event.getEventId());
        }
        try {
            SensorEventDocument doc = SensorEventDocument.fromDomain(event);
            SensorEventDocument savedDoc = repository.save(doc);
            return savedDoc.toDomain();
        } catch (DuplicateKeyException e) {
            throw new DuplicateEventException(event.getEventId());
        }
    }

    @Override
    public boolean existsByEventId(String eventId) {
        return repository.existsByEventId(eventId);
    }

    @Override
    public Optional<SensorEvent> findByEventId(String eventId) {
        return repository.findByEventId(eventId).map(SensorEventDocument::toDomain);
    }

    @Override
    public List<SensorEvent> findAll() {
        return repository.findAll().stream().map(SensorEventDocument::toDomain).toList();
    }
}
