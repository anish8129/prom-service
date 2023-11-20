/* (C) Games24x7 */
package com.kafka.example.customcommit;

import com.kafka.consumer.RecordsByPartition;
import com.kafka.consumer.config.KafkaConsumerConfig;
import com.kafka.consumer.handler.KafkaMessageHandlerForCustomCommit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class KafkaCustomCommitTestMessageHandler extends KafkaMessageHandlerForCustomCommit<String, String> {

    Logger LOGGER = LoggerFactory.getLogger(KafkaCustomCommitTestMessageHandler.class);
    private final List<String> message = new ArrayList<>();

    public KafkaCustomCommitTestMessageHandler(KafkaConsumerConfig consumerConfig) {
        super(consumerConfig);
    }

    @Override
    public void processMessage(ConsumerRecord<String, String> recordConsumer) {
        LOGGER.info("Consumer Reads records:{}", recordConsumer);
        String key = recordConsumer.key();
        String value = recordConsumer.value();
        if (key != null && value != null) {
            message.add(value);
        }
    }

    public List<String> getMessage() {
        return message;
    }

}
