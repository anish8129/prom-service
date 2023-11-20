/* (C) Games24x7 */
package com.kafka.example.autocommit;

import com.kafka.consumer.config.KafkaConsumerConfig;
import com.kafka.consumer.handler.KafkaMessageHandlerForAutoCommit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KafkaAutoCommitTestMessageHandler extends KafkaMessageHandlerForAutoCommit<String, String> {

    Logger LOGGER = LoggerFactory.getLogger(KafkaAutoCommitTestMessageHandler.class);
    private final List<String> message = new ArrayList<>();

    public KafkaAutoCommitTestMessageHandler(KafkaConsumerConfig consumerConfig) {
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
