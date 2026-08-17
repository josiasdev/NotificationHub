package com.cogitolab.notificationhub.infrastructure.adapter.in.rest;

import com.cogitolab.notificationhub.application.dto.NotificationDTO;
import com.cogitolab.notificationhub.domain.port.NotificationRepositoryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationRepositoryPort notificationRepository;

    public NotificationController(NotificationRepositoryPort notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> list = notificationRepository.findAll()
            .stream()
            .map(NotificationDTO::fromDomain)
            .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable String id) {
        return notificationRepository.findById(id)
            .map(n -> ResponseEntity.ok(NotificationDTO.fromDomain(n)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByEventId(@PathVariable String eventId) {
        List<NotificationDTO> list = notificationRepository.findByEventId(eventId)
            .stream()
            .map(NotificationDTO::fromDomain)
            .toList();
        return ResponseEntity.ok(list);
    }
}
