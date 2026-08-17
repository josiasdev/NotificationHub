package com.cogitolab.notificationhub.application.service.impl;

import com.cogitolab.notificationhub.domain.model.NotificationSeverity;
import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.model.SensorType;
import com.cogitolab.notificationhub.domain.rules.NotificationRule;
import com.cogitolab.notificationhub.domain.rules.RuleEvaluationResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SiloLevelRule implements NotificationRule {

    private static final BigDecimal THRESHOLD = new BigDecimal("15.0");

    @Override
    public boolean supports(SensorEvent event) {
        return event.getType() == SensorType.SILO_LEVEL;
    }

    @Override
    public RuleEvaluationResult evaluate(SensorEvent event) {
        if (event.getNumericValue() != null && event.getNumericValue().compareTo(THRESHOLD) < 0) {
            String message = String.format("⚠️ Nível baixo no silo: o silo monitorado por %s está com %s%% de sua capacidade.",
                event.getDeviceId(), event.getNumericValue());
            return RuleEvaluationResult.alert(NotificationSeverity.WARNING, message);
        }
        return RuleEvaluationResult.noAlert();
    }
}
