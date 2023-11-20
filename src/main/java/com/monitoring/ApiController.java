/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.monitoring;


import com.monitoring.model.WebClient;
import com.prometheus.ApiService;
import com.prometheus.ThreadLocalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("monitorApiController")
public class ApiController {

    private final ThreadLocalService threadLocalService;
    private final ApiService apiService;

    private final WebClient webClient;

    public ApiController(ThreadLocalService threadLocalService, ApiService apiService,
            WebClient webClient) {
        this.threadLocalService = threadLocalService;
        this.apiService = apiService;
        this.webClient = webClient;
    }

    @GetMapping("/getS")
    public String getS() {
        webClient.clientTest();
        return "Hello WorldS";
    }
}

