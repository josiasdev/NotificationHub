package com.cogitolab.notificationhub.infrastructure.adapter.in.rest;

import com.cogitolab.notificationhub.application.dto.SensorEventDTO;
import com.cogitolab.notificationhub.application.usecase.IngestSensorEventUseCase;
import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.port.SensorEventRepositoryPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final IngestSensorEventUseCase ingestSensorEventUseCase;
    private final SensorEventRepositoryPort sensorEventRepository;

    public EventController(IngestSensorEventUseCase ingestSensorEventUseCase,
                           SensorEventRepositoryPort sensorEventRepository) {
        this.ingestSensorEventUseCase = ingestSensorEventUseCase;
        this.sensorEventRepository = sensorEventRepository;
    }

    @PostMapping
    public ResponseEntity<SensorEventDTO> ingestEvent(@Valid @RequestBody SensorEventDTO dto) {
        SensorEvent event = ingestSensorEventUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(SensorEventDTO.fromDomain(event));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<SensorEventDTO>> ingestBatch(@Valid @RequestBody List<SensorEventDTO> dtos) {
        List<SensorEventDTO> results = new ArrayList<>();
        for (SensorEventDTO dto : dtos) {
            SensorEvent event = ingestSensorEventUseCase.execute(dto);
            results.add(SensorEventDTO.fromDomain(event));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }

    @GetMapping
    public ResponseEntity<List<SensorEventDTO>> getAllEvents() {
        List<SensorEventDTO> events = sensorEventRepository.findAll()
            .stream()
            .map(SensorEventDTO::fromDomain)
            .toList();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<SensorEventDTO> getEventByEventId(@PathVariable String eventId) {
        return sensorEventRepository.findByEventId(eventId)
            .map(event -> ResponseEntity.ok(SensorEventDTO.fromDomain(event)))
            .orElse(ResponseEntity.notFound().build());
    }
}
