/* (C) Games24x7 */
package com.kafka.container;

import org.springframework.stereotype.Component;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
public class KafkaTestContainer {

    private static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
            .withEmbeddedZookeeper()
            .withReuse(true)
            .withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofSeconds(5)));

    @PostConstruct
    void init() {
        kafka.start();
    }

    public String bootstrapServer() {
        return kafka.getBootstrapServers();
    }
}
