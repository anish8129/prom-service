/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.monitoring;

import org.aspectj.lang.ProceedingJoinPoint;

public interface MonitoringAspect {

    Object publish(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

    /**
     * @return the type of monitoring, should be non null
     */
    String type();

}
