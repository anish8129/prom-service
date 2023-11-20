/* (C) Games24x7 */

package com.redis.util;

import com.redis.config.RedisClientConfig;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import redis.clients.jedis.HostAndPortMapper;

import java.time.Duration;

public class RedisBeanUtils {

    public static RedisConnectionFactory createLettuceConnectionFactory(RedisClientConfig config,
                                                                        ClientResources clientResources,
                                                                        ClientOptions clientOptions) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(getConnectionPoolingConfig(config))
                .clientResources(clientResources)
                .clientOptions(clientOptions)
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .commandTimeout(Duration.ofMillis(config.getReadTimeOut()))
                .build();

        if (config.isClusterEnabled()) {
            return new LettuceConnectionFactory(getClusterConfig(config), clientConfiguration);
        }

        return new LettuceConnectionFactory(getStandAloneConfiguration(config), clientConfiguration);
    }

    public static RedisConnectionFactory createJedisConnectionFactory(RedisClientConfig config, HostAndPortMapper hostAndPortMapper) {
        JedisClientConfiguration clientConfiguration = JedisClientConfiguration.builder()
                .readTimeout(Duration.ofMillis(config.getReadTimeOut()))
                .connectTimeout(Duration.ofMillis(config.getConnectionTimeOut()))
                .hostAndPortMapper(hostAndPortMapper)
                .usePooling()
                .poolConfig(getConnectionPoolingConfig(config))
                .build();

        if (config.isClusterEnabled()) {
            return new JedisConnectionFactory(getClusterConfig(config), clientConfiguration);
        }

        return new JedisConnectionFactory(getStandAloneConfiguration(config), clientConfiguration);

    }

    private static RedisClusterConfiguration getClusterConfig(RedisClientConfig config) {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setUsername(config.getUserName());
        redisClusterConfiguration.setPassword(RedisPassword.of(config.getPassword()));
        redisClusterConfiguration.setClusterNodes(config.getCluster());
        redisClusterConfiguration.setMaxRedirects(config.getMaxRedirects());
        return redisClusterConfiguration;
    }

    private static GenericObjectPoolConfig<?> getConnectionPoolingConfig(RedisClientConfig config) {
        GenericObjectPoolConfig<?> connectionPooling = new GenericObjectPoolConfig<>();
        connectionPooling.setMaxTotal(config.getMaxActive());
        connectionPooling.setMaxIdle(config.getMaxIdle());
        connectionPooling.setMinIdle(config.getMinIdle());
        connectionPooling.setMaxWait(Duration.ofMillis(config.getMaxWaitInMillis()));
        return connectionPooling;
    }

    private static RedisStandaloneConfiguration getStandAloneConfiguration(RedisClientConfig config) {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setUsername(config.getUserName());
        standaloneConfiguration.setPassword(RedisPassword.of(config.getPassword()));
        standaloneConfiguration.setHostName(config.getHost());
        standaloneConfiguration.setPort(config.getPort());
        return standaloneConfiguration;
    }
}