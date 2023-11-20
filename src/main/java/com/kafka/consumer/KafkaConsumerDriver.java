/* (C) Games24x7 */
package com.kafka.consumer;

import com.kafka.consumer.config.KafkaConsumerConfig;
import com.kafka.consumer.handler.KafkaMessageHandler;
import com.kafka.consumer.listener.CustomRebalanceHandlerForCustomCommit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.internals.NoOpConsumerRebalanceListener;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;

public class KafkaConsumerDriver<K, V> {
    Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerDriver.class);
    private final KafkaConsumerConfig consumerConfig;
    private final KafkaMessageHandler<K, V> kafkaMessageHandler;

    private final Deserializer<K> keySerializer;
    private final Deserializer<V> valueSerializer;

    private ConsumerRebalanceListener consumerRebalanceListener ;

    public KafkaConsumerDriver(KafkaConsumerConfig consumerConfig,
                               KafkaMessageHandler<K, V> kafkaMessageHandler) {
        this.consumerConfig = consumerConfig;
        this.kafkaMessageHandler = kafkaMessageHandler;
        keySerializer = consumerConfig.getKeySerializer();
        valueSerializer = consumerConfig.getValueSerializer();
    }

    @PostConstruct
    void init() {
        Properties props = generateProps(consumerConfig);
        new Thread(() -> startConsumer(props)).start();
    }

    private Properties generateProps(KafkaConsumerConfig consumerConfig) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.getBootStrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfig.getGroupId());
        setAdditionalConfigsIfAny(props, consumerConfig);
        return props;
    }


    public void startConsumer(Properties props) {
        KafkaConsumer<K, V> consumer = new KafkaConsumer<>(
                props,
                keySerializer,
                valueSerializer
        );
        kafkaMessageHandler.setConsumer(consumer);

        if (consumerConfig.useCustomRebalance()) {
            consumerRebalanceListener = new CustomRebalanceHandlerForCustomCommit<>(consumer, kafkaMessageHandler);
        }

        try {
            // Subscribe to the topic(s) you want to consume
            ConsumerRebalanceListener rebalanceListener = consumerRebalanceListener == null ?
                    new NoOpConsumerRebalanceListener() : consumerRebalanceListener;

            consumer.subscribe(consumerConfig.getTopic(), rebalanceListener);
            if (consumerConfig.isEnabled()) {
                while (consumerConfig.isEnabled()) {
                    ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(100));
                    kafkaMessageHandler.handleMessage(records);
                }
            } else {
                LOGGER.warn("Consumer Not Enabled");
            }

        } catch (WakeupException exception) {
            // if (!stopped.get())
            throw exception;
        } finally {
            consumer.close();
        }
    }

    // Method to set extra config which will get directly from Zookeeper
    private void setAdditionalConfigsIfAny(Properties props, KafkaConsumerConfig consumerConfig) {

        Optional.ofNullable(consumerConfig.isAutoCommitEnabled())
                .ifPresent( val -> props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, val));

        for (String keyValue : consumerConfig.getAdditionalProperties()) {
            String[] split = keyValue.split(":");
            if (split.length > 1) {
                props.put(split[0], split[1]);
            }
        }
    }
}
