/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.monitoring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class AbstractMonitoringAspect implements MonitoringAspect {

    @Around("getExpression()")
    public Object publish(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return monitor(proceedingJoinPoint);
    }

    protected abstract Object monitor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

    @Pointcut("(bean(*Controller)" +
            " || bean(*Client) " +
            " || within(com.games24x7..RestCoreUtil))" +
            " && execution(public * *(..))")
    protected void getExpression(){}
}
