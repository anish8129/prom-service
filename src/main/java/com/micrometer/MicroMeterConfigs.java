/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.micrometer;

import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class MicroMeterConfigs {
    private static List<String> filter = Arrays.asList("logback", "http", "process.file", "tomcat");

//    @Bean
//    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
//        return registry -> {
//            registry.config()
//                    .meterFilter(MeterFilter.deny(id -> provider(id)));
//        };
//    }

    private boolean provider(Id id) {
        return filter.stream()
                .filter(d -> id.getName().startsWith(d))
                .map(d -> true)
                .findFirst()
                .orElse(false);
    }
}
