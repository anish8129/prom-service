/* (C) Games24x7 */

package com.redis.container.cluster;

import com.redis.config.RedisClientConfig;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.SocketAddressResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.utility.DockerImageName;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.HostAndPortMapper;

import javax.annotation.PreDestroy;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Import({
        RedisClusterTestContainer.LettuceTestClusterClientConfig.class,
        RedisClusterTestContainer.JedisTestClusterClientConfig.class
})
public class RedisClusterTestContainer {
    private static final GenericContainer<?> redis;
    private static final String REDIS_IMAGE = "grokzen/redis-cluster:6.0.7";
    private static final Set<Integer> CLUSTER_PORTS = Set.of(7000, 7001, 7002, 7003, 7004, 7005);
    private static final List<String> HOST_PORT = new ArrayList<>();

    private static final ConcurrentMap<Integer, Integer> CLUSTER_NAT_PORT_MAPPING = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, SocketAddress> CLUSTER_SOCKET_ADDRESS = new ConcurrentHashMap<>();

    static {
        redis = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
                .withReuse(true)
                .withExposedPorts(CLUSTER_PORTS.toArray(new Integer[0]))
                .withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofSeconds(10)));
        redis.start();
        CLUSTER_PORTS.forEach(port -> {
            Integer mappedPort = redis.getMappedPort(port);
            CLUSTER_NAT_PORT_MAPPING.put(port, mappedPort);
            HOST_PORT.add(String.format("%s:%d", redis.getHost(), mappedPort));
        });
    }

    @PreDestroy
    void tearDown() {
        redis.stop();
    }


    @Bean
    RedisScript<List> clusterTestScript() {
        return RedisScript.of(new ClassPathResource("redis/local-testing.lua"), List.class);
    }

    @Configuration
    @ConditionalOnExpression("'${client.type}'=='LETTUCE'" +
            "&& '${redis.mode}'=='CLUSTER'"
    )
    public static class LettuceTestClusterClientConfig {
        @Bean
        RedisClientConfig lettuceClusterTestClientConfig() {
            return new LettuceTestClusterConfig(HOST_PORT);
        }

        @Primary
        @Bean
        public ClientResources redisClientResources() {
            final SocketAddressResolver socketAddressResolver = new SocketAddressResolver() {
                @Override
                public SocketAddress resolve(RedisURI redisURI) {
                    Integer mappedPort = CLUSTER_NAT_PORT_MAPPING.get(redisURI.getPort());
                    if (mappedPort != null) {
                        SocketAddress socketAddress = CLUSTER_SOCKET_ADDRESS.get(mappedPort);
                        if (socketAddress != null) {
                            return socketAddress;
                        }
                        redisURI.setPort(mappedPort);
                    }
                    redisURI.setHost(DockerClientFactory.instance().dockerHostIpAddress());
                    SocketAddress socketAddress = super.resolve(redisURI);
                    CLUSTER_SOCKET_ADDRESS.putIfAbsent(redisURI.getPort(), socketAddress);
                    return socketAddress;
                }
            };
            return ClientResources.builder().socketAddressResolver(socketAddressResolver).build();
        }
    }

    @Configuration
    @ConditionalOnExpression("'${client.type}'=='JEDIS'" +
            "&& '${redis.mode}'=='CLUSTER'"
    )
    public static class JedisTestClusterClientConfig {
        @Bean
        RedisClientConfig jedisClusterTestClientConfig() {
            return new JedisTestClusterConfig(HOST_PORT);
        }


        @Bean
        @Primary
        HostAndPortMapper testHostAndPortMapper() {
            return new HostAndPortMapper() {
                @Override
                public HostAndPort getHostAndPort(HostAndPort hap) {
                    Integer mapperPort = CLUSTER_NAT_PORT_MAPPING.get(hap.getPort());
                    if (mapperPort != null) {
                        return new HostAndPort(DockerClientFactory.instance().dockerHostIpAddress(), mapperPort);
                    }
                    return hap;
                }
            };
        }
    }
}
