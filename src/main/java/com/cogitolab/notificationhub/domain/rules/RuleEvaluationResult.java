package com.cogitolab.notificationhub.domain.rules;

import com.cogitolab.notificationhub.domain.model.NotificationSeverity;

public record RuleEvaluationResult(
    boolean triggered,
    NotificationSeverity severity,
    String message
) {
    public static RuleEvaluationResult noAlert() {
        return new RuleEvaluationResult(false, null, null);
    }

    public static RuleEvaluationResult alert(NotificationSeverity severity, String message) {
        return new RuleEvaluationResult(true, severity, message);
    }
}
