package ru.job4j.bmb.conditions;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

public class TelegramRealCondition implements Condition {
    public TelegramRealCondition() {
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var mode =  context.getEnvironment().getProperty("telegram.mode");
        return !Objects.equals("fake", mode);
    }
}
