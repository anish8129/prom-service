/* (C) Games24x7 */
package com.kafka.consumer.handler;

import com.kafka.consumer.RecordsByPartition;
import com.kafka.consumer.config.KafkaConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class KafkaMessageHandlerForAutoCommit<K, V> implements KafkaMessageHandler<K, V> {

    private final ExecutorService executorService;

    private KafkaConsumer<K, V> consumer;

    public KafkaMessageHandlerForAutoCommit(KafkaConsumerConfig consumerConfig) {
        executorService = Executors.newFixedThreadPool(consumerConfig.threadCount());
    }

    public void handleMessage(ConsumerRecords<K, V> records) {
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<K, V>> partitionSpecificRecords = records.records(partition);
            RecordsByPartition<K, V> recordsByPartition = new RecordsByPartition<>(partitionSpecificRecords, this::processMessage);
            executorService.submit(recordsByPartition);
        }
        checkActiveTasks();
    }

    public abstract void processMessage(ConsumerRecord<K, V> recordConsumer);

    @Override
    public void setConsumer(KafkaConsumer<K, V> consumer) {
        this.consumer = consumer;
    }

    @Override
    public KafkaConsumer<K, V> getConsumer() {
        return consumer;
    }
}
