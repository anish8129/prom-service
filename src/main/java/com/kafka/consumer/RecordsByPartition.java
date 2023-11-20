/* (C) Games24x7 */
package com.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class RecordsByPartition<K, V> implements Runnable {
    private final List<ConsumerRecord<K, V>> records;
    private volatile boolean stopped = false;
    private volatile boolean started = false;
    private final CompletableFuture<Long> completion = new CompletableFuture<>();
    private volatile boolean finished = false;
    private final ReentrantLock startStopLock = new ReentrantLock();
    private final AtomicLong currentOffset = new AtomicLong(-1);

    private final Consumer<ConsumerRecord<K, V>> messageProcessor;

    public RecordsByPartition(List<ConsumerRecord<K,V>> partitionSpecificRecords, Consumer<ConsumerRecord<K, V>> messageProcessor) {
        records = partitionSpecificRecords;
        this.messageProcessor = messageProcessor;
    }


    public void run() {
        startStopLock.lock();
        if (stopped){
            return;
        }
        started = true;
        startStopLock.unlock();
        for (ConsumerRecord<K, V> record : records) {
            if (stopped)
                break;
            messageProcessor.accept(record);
            currentOffset.set(record.offset() + 1);
        }
        finished = true;
        completion.complete(currentOffset.get());
    }

    public void stop() {
        startStopLock.lock();
        this.stopped = true;
        if (!started) {
            finished = true;
            completion.complete(-1L);
        }
        startStopLock.unlock();
    }

    public long waitForCompletion() {
        try {
            return completion.get();
        } catch (InterruptedException | ExecutionException e) {
            return -1;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public long getCurrentOffset() {
        return currentOffset.get();
    }
}
