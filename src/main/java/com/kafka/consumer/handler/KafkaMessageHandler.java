/* (C) Games24x7 */
package com.kafka.consumer.handler;

import com.kafka.consumer.RecordsByPartition;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface KafkaMessageHandler<K, V> {

     Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new ConcurrentHashMap<>();

     Map<TopicPartition, RecordsByPartition<?, ?>> activeTasks = new HashMap<>();

    void handleMessage(ConsumerRecords<K, V> records);

    void setConsumer(KafkaConsumer<K, V> consumer);

    KafkaConsumer<K, V> getConsumer();

    default void checkActiveTasks() {
        List<TopicPartition> finishedTasksPartitions = new ArrayList<>();
        activeTasks.forEach((partition, task) -> {
            if (task.isFinished())
                finishedTasksPartitions.add(partition);
            long offset = task.getCurrentOffset();
            if (offset > 0)
                offsetsToCommit.put(partition, new OffsetAndMetadata(offset));
        });
        finishedTasksPartitions.forEach(activeTasks::remove);
        getConsumer().resume(finishedTasksPartitions);
    }
}
