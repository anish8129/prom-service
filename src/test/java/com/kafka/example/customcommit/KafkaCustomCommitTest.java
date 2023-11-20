/* (C) Games24x7 */
package com.kafka.example.customcommit;

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
@Import(KafkaCustomCommitConsumerProducerTestConfig.class)
public class KafkaCustomCommitTest {

    @Autowired
    KafkaProducerDriver kafkaProducerDriver;

    @Autowired
    KafkaCustomCommitTestMessageHandler kafkaCustomCommitTestMessageHandler;

    @Test
    void testProduceAndConsumeKafkaMessage() {
        kafkaProducerDriver.send("A", "Apple");
        kafkaProducerDriver.send("B", "Ball");
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> assertThat(kafkaCustomCommitTestMessageHandler.getMessage().size()).isEqualTo(2));
        Assertions.assertEquals(2, kafkaCustomCommitTestMessageHandler.getMessage().size());
        Assertions.assertEquals("Apple", kafkaCustomCommitTestMessageHandler.getMessage().get(0));
        Assertions.assertEquals("Ball", kafkaCustomCommitTestMessageHandler.getMessage().get(1));
    }
}
