package com.cogitolab.notificationhub.infrastructure.adapter.out.persistence;

import com.cogitolab.notificationhub.domain.model.Notification;
import com.cogitolab.notificationhub.domain.port.NotificationRepositoryPort;
import com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.document.NotificationDocument;
import com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.repository.SpringDataMongoNotificationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class NotificationMongoAdapter implements NotificationRepositoryPort {

    private final SpringDataMongoNotificationRepository repository;

    public NotificationMongoAdapter(SpringDataMongoNotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification save(Notification notification) {
        NotificationDocument doc = NotificationDocument.fromDomain(notification);
        NotificationDocument saved = repository.save(doc);
        return saved.toDomain();
    }

    @Override
    public Optional<Notification> findById(String id) {
        return repository.findById(id).map(NotificationDocument::toDomain);
    }

    @Override
    public List<Notification> findByEventId(String eventId) {
        return repository.findByEventId(eventId).stream().map(NotificationDocument::toDomain).toList();
    }

    @Override
    public List<Notification> findAll() {
        return repository.findAll().stream().map(NotificationDocument::toDomain).toList();
    }
}
