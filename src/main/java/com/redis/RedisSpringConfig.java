/* (C) Games24x7 */

package com.redis;

import com.redis.config.RedisClientConfig;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.HostAndPortMapper;

import static com.redis.config.RedisClientConfig.LETTUCE_AS_CLIENT;
import static com.redis.util.RedisBeanUtils.createJedisConnectionFactory;
import static com.redis.util.RedisBeanUtils.createLettuceConnectionFactory;

@Configuration
@ComponentScan
public class RedisSpringConfig {
    @Bean
    StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    RedisConnectionFactory connectionFactory(RedisClientConfig config,
                                             ClientResources clientResources,
                                             ClientOptions clientOptions,
                                             HostAndPortMapper hostAndPortMapper) {
        if (LETTUCE_AS_CLIENT.equals(config.client())) {
            return createLettuceConnectionFactory(
                    config,
                    clientResources,
                    clientOptions
            );
        }
        return createJedisConnectionFactory(config, hostAndPortMapper);
    }

    @Bean
    HostAndPortMapper defaultHostAndPortMapper() {
        return new HostAndPortMapper() {
            @Override
            public HostAndPort getHostAndPort(HostAndPort hap) {
                return hap;
            }
        };
    }

    @Bean
    public ClientResources createClientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    ClientOptions createClientOptions() {
        return ClientOptions.builder()
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true).build();
    }
}
