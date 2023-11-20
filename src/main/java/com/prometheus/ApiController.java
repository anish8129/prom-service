/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.prometheus;

import static java.lang.Math.random;

import com.games24x7.RestCoreUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final ThreadLocalService threadLocalService;
    private final ApiService apiService;

    private final RestCoreUtil restCoreUtil;

    private final List<InfoContributor> infoContributorList;

    public ApiController(ThreadLocalService threadLocalService, ApiService apiService,
            RestCoreUtil restCoreUtil,
            List<InfoContributor> infoContributorList) {
        this.threadLocalService = threadLocalService;
        this.apiService = apiService;
        this.restCoreUtil = restCoreUtil;
        this.infoContributorList = infoContributorList;
    }

    private String apply(InfoContributor c) {
        return c.getClass().getSimpleName();
    }

    @GetMapping("/getString/{name}")
    public String getString(@PathVariable String name) {
        restCoreUtil.test();
        return "Hello World" + name;
    }

    @GetMapping("/infoContri")
    public Object getInfoContri() {
        return infoContributorList.stream()
                .map(this::apply)
                .collect(Collectors.toList());
    }

    @GetMapping("/testThreadLocal")
    public Employee getEmployee() {
        double v = random() * 100;
        System.out.println("Setting Thread Local value as " + v);
        threadLocalService.setThreadLocal(v);
        return apiService.fetchEmployee();
    }

    @PostMapping("/getEmp/{id}")
    public String getEmp(@PathVariable String id, @RequestBody Employee payLoad) {
        return "Hello World";
    }
}
