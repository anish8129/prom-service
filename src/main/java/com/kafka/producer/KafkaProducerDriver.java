/* (C) Games24x7 */
package com.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;

public class KafkaProducerDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerDriver.class);

    private KafkaProducer<String, String> producer;
    private final String bootstrapServer;

    private final String topic;

    public KafkaProducerDriver(String bootstrapServer, String topic) {
        this.bootstrapServer = bootstrapServer;
        this.topic = topic;
    }

    @PostConstruct
    void init() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer); // Set your Kafka broker(s)
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 5);

        // Create a Kafka producer instance
        producer = new KafkaProducer<>(props);

        // Define the topic to which you want to send messages
    }

    public void send(String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        LOGGER.info("Producing Message to topic:{} key:{} value:{}", topic, key, value);
        producer.send(record);
    }

    @PreDestroy
    void shutdown() {
        producer.close();
    }
}
