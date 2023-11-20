/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.monitoring;

import com.monitoring.model.Events;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingAspect extends AbstractMonitoringAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
    private final HttpServletRequest request;

    public LoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    public Object monitor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object[] args = proceedingJoinPoint.getArgs();
        Signature signature = proceedingJoinPoint.getSignature();
        Object proceed = proceedingJoinPoint.proceed();
        long processingTime = System.currentTimeMillis() - startTime;
        logCalledApiDetails(args, signature, processingTime);
        return proceed;
    }

    private void logCalledApiDetails(Object[] args, Signature signature, long processingTime) {
        Events events = new Events("PROMETHEUS");
        events.setUri(request.getRequestURL().toString());
        events.setSignature(signature.toString());
        events.setMethodArguments(Arrays.asList(args));
        events.setProcessingTimeInMillis(processingTime);
        LOGGER.info("Event logged:{}", events);
    }

    @Override
    protected void getExpression() {}

    @Override
    public String type() {
        return "LOG";
    }
}
