package com.redis.config;

import org.springframework.data.redis.connection.RedisNode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base Client Config which can be used for creating the RedisConnection Configuration
 */
public interface RedisClientConfig {

    /**
     * When we want to use Lettuce as a client
     * Make sure to add the dependency on the class path
     */
    String LETTUCE_AS_CLIENT = "LETTUCE";

    /**
     * When we want to use Lettuce as a client
     * Make sure to add the dependency on the class path
     */
    String JEDIS_AS_CLIENT = "JEDIS";

    /**
     * CLuster Config
     * Default Config Used for the Redirection when data not found in the requested cluster
     */
    int DEFAULT_MAX_REDIRECTS = 2;

    /**
     * Connection Pooling Configs
     */
    int DEFAULT_MAX_IDLE = 2;
    int DEFAULT_MIN_IDLE = 2;
    int DEFAULT_MAX_ACTIVE_CONNECTION = 4;
    int DEFAULT_MAX_WAIT_IN_MILLIS = 5000;

    /**
     * Client Configuration
     */
    int DEFAULT_CONNECTION_TIMEOUT = 10000;
    int DEFAULT_READ_TIMEOUT = 10000;
    String DELIMITER = ":";

    String getUserName();

    String getPassword();

    default Set<RedisNode> getCluster() {
        return getClusterHostAndPort()
                .stream()
                .map(this::createRedisNode)
                .collect(Collectors.toSet());
    }


    List<String> getClusterHostAndPort();

    int getMaxRedirects();

    String client();

    String getHost();

    int getPort();

    boolean isClusterEnabled();

    int getMaxActive();

    int getMaxIdle();

    int getMinIdle();

    int getMaxWaitInMillis();

    int getConnectionTimeOut();

    int getReadTimeOut();

    default RedisNode createRedisNode(String hostAndPort) {
        String[] values = hostAndPort.split(DELIMITER);
        String host = values[0].trim();
        int port = Integer.parseInt(values[1].trim());
        return new RedisNode(host, port);
    }
}