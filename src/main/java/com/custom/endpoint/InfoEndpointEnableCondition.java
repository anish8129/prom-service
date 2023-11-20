/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.custom.endpoint;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class InfoEndpointEnableCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return false;
    }
}
