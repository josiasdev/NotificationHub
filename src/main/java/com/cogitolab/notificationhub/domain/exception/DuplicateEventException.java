package com.cogitolab.notificationhub.domain.exception;

public class DuplicateEventException extends RuntimeException {
    public DuplicateEventException(String eventId) {
        super("Event with eventId '" + eventId + "' has already been processed.");
    }
}
