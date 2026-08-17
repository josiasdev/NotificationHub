package com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.repository;

import com.cogitolab.notificationhub.infrastructure.adapter.out.persistence.mongo.document.SensorEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataMongoEventRepository extends MongoRepository<SensorEventDocument, String> {
    boolean existsByEventId(String eventId);
    Optional<SensorEventDocument> findByEventId(String eventId);
}
