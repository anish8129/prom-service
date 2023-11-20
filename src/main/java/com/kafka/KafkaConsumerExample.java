package com.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaConsumerExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerExample.class);

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static KafkaConsumer<String, String> consumer;
    public final Map<TopicPartition, Task> activeTasks = new ConcurrentHashMap<>();

    public final Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new ConcurrentHashMap<>();
    private static long lastCommitTime;

    public void startConsumer() {
        // Configure the consumer properties
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // Set your Kafka broker(s)
        props.put("group.id", "my-consumer-group"); // Consumer group ID
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // Create a Kafka consumer instance
        consumer = new KafkaConsumer<>(props);

        try {
            // Subscribe to the topic(s) you want to consume
            consumer.subscribe(Collections.singletonList("my-topic"));

            // Start consuming messages
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // Poll for messages
                handleFetchedRecords(records);
                checkActiveTasks();
                commitOffsets();
            }
        } catch (WakeupException exception) {
            // if (!stopped.get())
                throw exception;
        } finally {
            consumer.close();
        }
    }

    private void handleFetchedRecords(ConsumerRecords<String, String> records) {
        if (records.count() > 0) {
            records.partitions().forEach(partition -> {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                Task task = new Task(partitionRecords);
                executorService.submit(task);
                activeTasks.put(partition, task);
            });
            consumer.pause(records.partitions());
        }
    }

    private void checkActiveTasks() {
        List<TopicPartition> finishedTasksPartitions = new ArrayList<>();
        activeTasks.forEach((partition, task) -> {
            if (task.isFinished())
                finishedTasksPartitions.add(partition);
            long offset = task.getCurrentOffset();
            if (offset > 0)
                offsetsToCommit.put(partition, new OffsetAndMetadata(offset));
        });
        finishedTasksPartitions.forEach(partition -> activeTasks.remove(partition));
        consumer.resume(finishedTasksPartitions);
    }

    private void commitOffsets() {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastCommitTime > 5000) {
                if(!offsetsToCommit.isEmpty()) {
                    consumer.commitAsync(offsetsToCommit, new OffsetCommitCallback() {
                        @Override
                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                            if (exception != null)
                                LOGGER.error("Offset commit with offsets {} failed", offsets, exception);
                        }
                    });
                    offsetsToCommit.clear();
                }
                lastCommitTime = currentTimeMillis;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to commit offsets!", e);
        }
    }
}
