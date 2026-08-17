package com.cogitolab.notificationhub.domain.port;

import com.cogitolab.notificationhub.domain.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryPort {
    Notification save(Notification notification);
    Optional<Notification> findById(String id);
    List<Notification> findByEventId(String eventId);
    List<Notification> findAll();
}
