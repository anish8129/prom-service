/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.prometheus;

import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final ThreadLocalService threadLocalService;

    public ApiService(ThreadLocalService threadLocalService) {
        this.threadLocalService = threadLocalService;
    }

    public Employee fetchEmployee() {
        Employee employee = new Employee();
        employee.id = threadLocalService.getThreadLocal();
        return employee;
    }
}
