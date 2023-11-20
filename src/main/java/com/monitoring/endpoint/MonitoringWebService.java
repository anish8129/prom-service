/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.monitoring.endpoint;

import com.monitoring.MonitoringAspect;
import com.monitoring.model.MonitoringDetails;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringWebService {

    private final List<MonitoringAspect> monitoringAspectList;

    public MonitoringWebService(List<MonitoringAspect> monitoringAspectList) {
        this.monitoringAspectList = monitoringAspectList;
    }

    @GetMapping("/monitoring/enabled")
    MonitoringDetails enabledMonitoring() {
        MonitoringDetails monitoringDetails = new MonitoringDetails();
        for (MonitoringAspect monitoringAspect : monitoringAspectList) {
            monitoringDetails.addMonitoringType(monitoringAspect.type());
        }
        return monitoringDetails;
    }
}
