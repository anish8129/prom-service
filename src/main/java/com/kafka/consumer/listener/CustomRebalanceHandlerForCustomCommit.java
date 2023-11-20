/* (C) Games24x7 */
package com.kafka.consumer.listener;

import com.kafka.consumer.RecordsByPartition;
import com.kafka.consumer.handler.KafkaMessageHandler;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomRebalanceHandlerForCustomCommit<K, V> implements ConsumerRebalanceListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRebalanceHandlerForCustomCommit.class);
    private final KafkaConsumer<K, V> consumer;

    private final KafkaMessageHandler<K, V> messageHandler;

    public CustomRebalanceHandlerForCustomCommit(KafkaConsumer<K, V> consumer, KafkaMessageHandler<K, V> messageHandler) {
        this.consumer = consumer;
        this.messageHandler = messageHandler;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        Map<TopicPartition, RecordsByPartition<?, ?>> stoppedTasks = new HashMap<>();
        for (TopicPartition partition : partitions) {
            RecordsByPartition<?,?> task = messageHandler.activeTasks.remove(partition);
            if (task != null) {
                task.stop();
                stoppedTasks.put(partition, task);
            }
        }
        // 2. Wait for stopped tasks to complete processing of current record
        stoppedTasks.forEach((partition, task) -> {
            long offset = task.waitForCompletion();
            if (offset > 0)
                messageHandler.offsetsToCommit.put(partition, new OffsetAndMetadata(offset));
        });
        // 3. collect offsets for revoked partitions
        Map<TopicPartition, OffsetAndMetadata> revokedPartitionOffsets = new HashMap<>();
        partitions.forEach( partition -> {
            OffsetAndMetadata offset = messageHandler.offsetsToCommit.remove(partition);
            if (offset != null)
                revokedPartitionOffsets.put(partition, offset);
        });
        // 4. commit offsets for revoked partitions
        try {
            consumer.commitSync(revokedPartitionOffsets);
        } catch (Exception e) {
            LOGGER.warn("Failed to commit offsets for revoked partitions!");
        }
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        LOGGER.info("Partition Assigned:{}", partitions);
    }
}
