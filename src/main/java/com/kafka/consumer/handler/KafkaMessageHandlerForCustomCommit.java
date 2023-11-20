/* (C) Games24x7 */
package com.kafka.consumer.handler;

import com.kafka.consumer.RecordsByPartition;
import com.kafka.consumer.config.KafkaConsumerConfig;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public abstract class KafkaMessageHandlerForCustomCommit<KEY, VAL> implements KafkaMessageHandler<KEY, VAL> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageHandlerForCustomCommit.class);

    private final ExecutorService executorService;

    private KafkaConsumer<KEY, VAL> consumer;

    private final AtomicLong lastCommitTime = new AtomicLong();

    public KafkaMessageHandlerForCustomCommit(KafkaConsumerConfig consumerConfig) {
        String nameFormat = consumerConfig.getName() + "-%d";
        executorService = Executors.newFixedThreadPool(consumerConfig.threadCount(),
                new ThreadFactoryBuilder().setNameFormat(nameFormat).build());
    }

    @Override
    public void handleMessage(ConsumerRecords<KEY, VAL> records) {
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<KEY, VAL>> partitionSpecificRecords = records.records(partition);
            RecordsByPartition<KEY, VAL> recordsByPartition = new RecordsByPartition<>(partitionSpecificRecords, this::processMessage);
            executorService.submit(recordsByPartition);
            activeTasks.put(partition, recordsByPartition);
        }
        consumer.pause(records.partitions());
        checkActiveTasks();
        commitOffsets();
    }

    private void commitOffsets() {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastCommitTime.get() > 1000) {
                if(!offsetsToCommit.isEmpty()) {
                    LOGGER.info("Offset commit with offsets {}", offsetsToCommit);
                    consumer.commitAsync(offsetsToCommit, new OffsetCommitCallback() {
                        @Override
                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                            if (exception != null)
                                LOGGER.error("Offset commit with offsets {} failed", offsets, exception);
                        }
                    });
                    offsetsToCommit.clear();
                }
                lastCommitTime.set(currentTimeMillis);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to commit offsets!", e);
        }
    }


    public abstract void processMessage(ConsumerRecord<KEY, VAL> recordConsumer);

    @Override
    public void setConsumer(KafkaConsumer<KEY, VAL> consumer) {
        this.consumer = consumer;
    }

    @Override
    public KafkaConsumer<KEY, VAL> getConsumer() {
        return consumer;
    }
}
