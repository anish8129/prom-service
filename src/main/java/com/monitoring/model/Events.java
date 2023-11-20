/*
 * Copyright 2023 Play Games24x7 Pvt. Ltd. All Rights Reserved
 */

package com.monitoring.model;

import java.util.List;

public class Events {

    private String serviceName;

    private String signature;

    private String uri;

    private List<Object> methodArguments;

    private double processingTimeInMillis;

    public Events(String serviceName) {
        this.serviceName = serviceName;
    }

    public Events() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Object> getMethodArguments() {
        return methodArguments;
    }

    public void setMethodArguments(List<Object> methodArguments) {
        this.methodArguments = methodArguments;
    }

    public double getProcessingTimeInMillis() {
        return processingTimeInMillis;
    }

    public void setProcessingTimeInMillis(double processingTimeInMillis) {
        this.processingTimeInMillis = processingTimeInMillis;
    }

    @Override
    public String toString() {
        return "Events{" +
                "serviceName='" + serviceName + '\'' +
                ", signature='" + signature + '\'' +
                ", uri='" + uri + '\'' +
                ", methodArguments=" + methodArguments +
                ", processingTimeInMillis=" + processingTimeInMillis +
                '}';
    }
}
