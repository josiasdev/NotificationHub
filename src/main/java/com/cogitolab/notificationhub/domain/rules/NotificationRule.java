package com.cogitolab.notificationhub.domain.rules;

import com.cogitolab.notificationhub.domain.model.SensorEvent;

public interface NotificationRule {
    boolean supports(SensorEvent event);
    RuleEvaluationResult evaluate(SensorEvent event);
}
