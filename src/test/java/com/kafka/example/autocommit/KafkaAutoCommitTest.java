/* (C) Games24x7 */
package com.kafka.example.autocommit;

import com.kafka.KafkaSpringConfig;
import com.kafka.producer.KafkaProducerDriver;
import com.redis.container.JedisTestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@JedisTestContainer
@ContextConfiguration(classes = {KafkaSpringConfig.class})
@Import(KafkaAutoCommitConsumerProducerTestConfig.class)
public class KafkaAutoCommitTest {

    @Autowired
    KafkaProducerDriver kafkaProducerDriver;

    @Autowired
    KafkaAutoCommitTestMessageHandler kafkaAutoCommitTestMessageHandler;

    @Test
    void testProduceAndConsumeKafkaMessage() {
        kafkaProducerDriver.send("A", "Apple");
        kafkaProducerDriver.send("B", "Ball");
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> assertThat(kafkaAutoCommitTestMessageHandler.getMessage().size()).isEqualTo(2));
        Assertions.assertEquals(2, kafkaAutoCommitTestMessageHandler.getMessage().size());
        Assertions.assertEquals("Apple", kafkaAutoCommitTestMessageHandler.getMessage().get(0));
        Assertions.assertEquals("Ball", kafkaAutoCommitTestMessageHandler.getMessage().get(1));
    }
}
