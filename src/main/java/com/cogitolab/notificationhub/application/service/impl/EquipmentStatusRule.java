package com.cogitolab.notificationhub.application.service.impl;

import com.cogitolab.notificationhub.domain.model.NotificationSeverity;
import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.model.SensorType;
import com.cogitolab.notificationhub.domain.rules.NotificationRule;
import com.cogitolab.notificationhub.domain.rules.RuleEvaluationResult;
import org.springframework.stereotype.Component;

@Component
public class EquipmentStatusRule implements NotificationRule {

    private static final String FAILURE_STATUS = "FAILURE";

    @Override
    public boolean supports(SensorEvent event) {
        return event.getType() == SensorType.EQUIPMENT_STATUS;
    }

    @Override
    public RuleEvaluationResult evaluate(SensorEvent event) {
        if (event.getTextValue() != null && FAILURE_STATUS.equalsIgnoreCase(event.getTextValue())) {
            String message = String.format("🚨 Falha de equipamento: foi detectada uma falha no equipamento %s.",
                event.getDeviceId());
            return RuleEvaluationResult.alert(NotificationSeverity.CRITICAL, message);
        }
        return RuleEvaluationResult.noAlert();
    }
}
