package com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.repository;

import com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.document.NotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataMongoNotificationRepository extends MongoRepository<NotificationDocument, String> {
    List<NotificationDocument> findByEventId(String eventId);
}
