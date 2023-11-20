/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.monitoring.model;

import java.util.ArrayList;
import java.util.List;

public class MonitoringDetails {

    private List<String> enabledMonitoring = new ArrayList<>();

    public List<String> getEnabledMonitoring() {
        return enabledMonitoring;
    }

    public void addMonitoringType(String type) {
            enabledMonitoring.add(type);
    }
}
