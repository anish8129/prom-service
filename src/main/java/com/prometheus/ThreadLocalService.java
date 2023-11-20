/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.prometheus;

import org.springframework.stereotype.Service;

@Service
public class ThreadLocalService {

    private Double threadLocal;

    public Double getThreadLocal() {
        return threadLocal;
    }

    public void setThreadLocal(Double threadLocal) {
        this.threadLocal = threadLocal;
    }
}
