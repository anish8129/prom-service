package com.funnel;

import java.util.function.BiConsumer;

public abstract class ApiExecutor implements BiConsumer<Object, Object> {
    @Override
    public void accept(Object receivedMessage, Object outPutMessage) {
        execute(receivedMessage, outPutMessage);
    }
    
    protected abstract void execute(Object receivedMessage, Object outPutMessage);
}