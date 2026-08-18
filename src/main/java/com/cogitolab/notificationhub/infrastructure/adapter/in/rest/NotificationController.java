package com.cogitolab.notificationhub.infrastructure.adapter.in.rest;

import com.cogitolab.notificationhub.application.dto.NotificationDTO;
import com.cogitolab.notificationhub.domain.port.NotificationRepositoryPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notificações e Alertas", description = "Endpoints para consulta do histórico e status de envio de notificações geradas")
public class NotificationController {

    private final NotificationRepositoryPort notificationRepository;

    public NotificationController(NotificationRepositoryPort notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    @Operation(summary = "Lista todas as notificações", description = "Retorna o histórico de todas as mensagens de alerta geradas pelo motor de regras.")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> list = notificationRepository.findAll()
            .stream()
            .map(NotificationDTO::fromDomain)
            .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca notificação por ID", description = "Retorna os detalhes de uma notificação específica pelo seu UUID.")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable String id) {
        return notificationRepository.findById(id)
            .map(n -> ResponseEntity.ok(NotificationDTO.fromDomain(n)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/event/{eventId}")
    @Operation(summary = "Busca notificações por eventId", description = "Retorna as notificações vinculadas a um evento de sensor específico.")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByEventId(@PathVariable String eventId) {
        List<NotificationDTO> list = notificationRepository.findByEventId(eventId)
            .stream()
            .map(NotificationDTO::fromDomain)
            .toList();
        return ResponseEntity.ok(list);
    }
}
