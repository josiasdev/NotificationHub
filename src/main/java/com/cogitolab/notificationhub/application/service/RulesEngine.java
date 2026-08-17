package com.cogitolab.notificationhub.application.service;

import com.cogitolab.notificationhub.domain.model.SensorEvent;
import com.cogitolab.notificationhub.domain.rules.NotificationRule;
import com.cogitolab.notificationhub.domain.rules.RuleEvaluationResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RulesEngine {

    private final List<NotificationRule> rules;

    public RulesEngine(List<NotificationRule> rules) {
        this.rules = rules;
    }

    public Optional<RuleEvaluationResult> evaluate(SensorEvent event) {
        for (NotificationRule rule : rules) {
            if (rule.supports(event)) {
                RuleEvaluationResult result = rule.evaluate(event);
                if (result.triggered()) {
                    return Optional.of(result);
                }
            }
        }
        return Optional.empty();
    }
}
