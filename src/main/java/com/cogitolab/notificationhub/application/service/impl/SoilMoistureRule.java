package com.cogitolab.notificationhub.application.service.impl;

import com.cogitolab.notificationhub.domain.model.NotificationSeverity;
import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.model.SensorType;
import com.cogitolab.notificationhub.domain.rules.NotificationRule;
import com.cogitolab.notificationhub.domain.rules.RuleEvaluationResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SoilMoistureRule implements NotificationRule {

    private static final BigDecimal THRESHOLD = new BigDecimal("20.0");

    @Override
    public boolean supports(SensorEvent event) {
        return event.getType() == SensorType.SOIL_MOISTURE;
    }

    @Override
    public RuleEvaluationResult evaluate(SensorEvent event) {
        if (event.getNumericValue() != null && event.getNumericValue().compareTo(THRESHOLD) < 0) {
            String message = String.format("💧 Alerta de irrigação: a umidade do solo está em %s%%. Verifique a necessidade de irrigação.",
                event.getNumericValue());
            return RuleEvaluationResult.alert(NotificationSeverity.WARNING, message);
        }
        return RuleEvaluationResult.noAlert();
    }
}
