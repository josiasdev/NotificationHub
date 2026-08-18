package com.cogitolab.notificationhub.infrastructure.adapter.in.rest;

import com.cogitolab.notificationhub.application.dto.SensorEventDTO;
import com.cogitolab.notificationhub.application.usecase.IngestSensorEventUseCase;
import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.port.SensorEventRepositoryPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Sensores e Eventos IoT", description = "Endpoints para ingestão, simulação em lote e consulta de eventos de sensores")
public class EventController {

    private final IngestSensorEventUseCase ingestSensorEventUseCase;
    private final SensorEventRepositoryPort sensorEventRepository;

    public EventController(IngestSensorEventUseCase ingestSensorEventUseCase,
                           SensorEventRepositoryPort sensorEventRepository) {
        this.ingestSensorEventUseCase = ingestSensorEventUseCase;
        this.sensorEventRepository = sensorEventRepository;
    }

    @PostMapping
    @Operation(summary = "Ingere uma nova leitura de sensor", description = "Valida os dados do evento, garante idempotência via eventId, salva no MongoDB e aciona o motor de regras.")
    public ResponseEntity<SensorEventDTO> ingestEvent(@Valid @RequestBody SensorEventDTO dto) {
        SensorEvent event = ingestSensorEventUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(SensorEventDTO.fromDomain(event));
    }

    @PostMapping("/batch")
    @Operation(summary = "Ingere uma lista de eventos em lote (Simulador)", description = "Permite o envio de múltiplos eventos de sensores (massa de testes do edital) em uma única requisição.")
    public ResponseEntity<List<SensorEventDTO>> ingestBatch(@Valid @RequestBody List<SensorEventDTO> dtos) {
        List<SensorEventDTO> results = new ArrayList<>();
        for (SensorEventDTO dto : dtos) {
            SensorEvent event = ingestSensorEventUseCase.execute(dto);
            results.add(SensorEventDTO.fromDomain(event));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }

    @GetMapping
    @Operation(summary = "Lista todos os eventos recebidos", description = "Retorna o histórico completo dos eventos armazenados no MongoDB.")
    public ResponseEntity<List<SensorEventDTO>> getAllEvents() {
        List<SensorEventDTO> events = sensorEventRepository.findAll()
            .stream()
            .map(SensorEventDTO::fromDomain)
            .toList();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Busca evento por eventId", description = "Retorna os detalhes de um evento de sensor específico.")
    public ResponseEntity<SensorEventDTO> getEventByEventId(@PathVariable String eventId) {
        return sensorEventRepository.findByEventId(eventId)
            .map(event -> ResponseEntity.ok(SensorEventDTO.fromDomain(event)))
            .orElse(ResponseEntity.notFound().build());
    }
}
