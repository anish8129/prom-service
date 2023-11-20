/* (C) Games24x7 */

package com.redis.container.standalone;

import com.redis.RedisSpringConfig;
import com.redis.config.RedisClientConfig;
import com.redis.config.jedis.JedisDefaultClientConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static com.redis.config.RedisClientConfig.JEDIS_AS_CLIENT;
import static com.redis.config.RedisClientConfig.LETTUCE_AS_CLIENT;

@Import(RedisTestContainer.RedisTestClientConfig.class)
@ContextConfiguration(classes = RedisSpringConfig.class)
public class RedisTestContainer {
    private static final GenericContainer<?> redis;

    static {
        redis = new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine"))
                .withReuse(true)
                .withExposedPorts(6379);
        redis.start();
    }


    @Configuration
    @ConditionalOnProperty(value = "redis.mode", havingValue = "STANDALONE")
    public static class RedisTestClientConfig {
        @Bean
        @ConditionalOnProperty(value = "client.type", havingValue = LETTUCE_AS_CLIENT)
        RedisClientConfig lettuceTestClientConfig() {
            return new JedisDefaultClientConfig(redis.getHost(),
                    redis.getMappedPort(6379)
            );
        }

        @Bean
        @ConditionalOnProperty(value = "client.type", havingValue = JEDIS_AS_CLIENT)
        RedisClientConfig jedisTestClientConfig() {
            return new JedisDefaultClientConfig(redis.getHost(),
                    redis.getMappedPort(6379)
            );
        }

        @Bean
        RedisScript<List> testScript() {
            return RedisScript.of(new ClassPathResource("redis/local-testing.lua"), List.class);
        }
    }
}
