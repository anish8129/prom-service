/* (C) Games24x7 */
package com.kafka.example.customcommit;

import com.kafka.consumer.KafkaConsumerDriver;
import com.kafka.consumer.config.KafkaConsumerConfig;
import com.kafka.container.KafkaTestContainer;
import com.kafka.producer.KafkaProducerDriver;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaCustomCommitConsumerProducerTestConfig {

    private static final String TEST_TOPIC = "test-custom-commit-topic";
    private final KafkaTestContainer kafkaTestContainer;

    public KafkaCustomCommitConsumerProducerTestConfig(KafkaTestContainer kafkaTestContainer) {
        this.kafkaTestContainer = kafkaTestContainer;
    }

    @PostConstruct
    void init() {
        Properties p = new Properties();
        p.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaTestContainer.bootstrapServer());
        AdminClient client = AdminClient.create(p);
        NewTopic newTopic = new NewTopic(TEST_TOPIC, 3, (short) 2);
        client.createTopics(List.of(newTopic));
    }

    @Bean
    @Primary
    KafkaConsumerConfig consumerConfig() {
        return new KafkaConsumerConfig() {

            @Override
            public String getBootStrapServers() {
                return kafkaTestContainer.bootstrapServer();
                //return String.format("PLAINTEXT://%s:%s", kafka.getHost(), kafka.getMappedPort(9093));
            }

            @Override
            public String getGroupId() {
                return "test-custom-commit";
            }

            @Override
            public Deserializer<?> getKeySerializer() {
                return new StringDeserializer();
            }

            @Override
            public Deserializer<?> getValueSerializer() {
                return new StringDeserializer();
            }

            @Override
            public Collection<String> getTopic() {
                return Collections.singleton(TEST_TOPIC);
            }

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public Boolean isAutoCommitEnabled() {
                return false;
            }

            @Override
            public int threadCount() {
                return 2;
            }

            @Override
            public String[] getAdditionalProperties() {
                return new String[]{"auto.offset.reset:earliest"};
            }

            @Override
            public String getName() {
                return "test-custom-commit-kafka-consumer";
            }

            @Override
            public Boolean useCustomRebalance() {
                return true;
            }
        };
    }


    @Bean
    @Primary
    KafkaProducerDriver kafkaProducerDriver() {
        return new KafkaProducerDriver(
                kafkaTestContainer.bootstrapServer(),
                TEST_TOPIC);
    }

    @Bean
    @Primary
    KafkaConsumerDriver<String, String> consumer(KafkaConsumerConfig consumerConfig, KafkaCustomCommitTestMessageHandler messageHandler) {
        return new KafkaConsumerDriver<>(consumerConfig, messageHandler);
    }
}
