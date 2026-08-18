package com.cogitolab.notificationhub.infrastructure.adapter.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        return ResponseEntity.ok(Map.of(
            "application", "NotificationHub",
            "version", "1.0.0",
            "status", "UP",
            "endpoints", Map.of(
                "events", "/api/v1/events",
                "events_batch", "/api/v1/events/batch",
                "notifications", "/api/v1/notifications"
            )
        ));
    }
}
