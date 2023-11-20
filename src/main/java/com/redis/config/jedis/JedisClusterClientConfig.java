/* (C) Games24x7 */

package com.redis.config.jedis;

import com.redis.config.RedisClientConfig;

/**
 * Cluster Based Config for using the Jedis Client
 */
public abstract class JedisClusterClientConfig implements RedisClientConfig {

    @Override
    public boolean isClusterEnabled() {
        return true;
    }

    @Override
    public int getMaxRedirects() {
        return DEFAULT_MAX_REDIRECTS;
    }

    @Override
    public String client() {
        return JEDIS_AS_CLIENT;
    }

    @Override
    public String getHost() {
        throw new UnsupportedOperationException("For Cluster Config Not Needed");
    }

    @Override
    public int getPort() {
        throw new UnsupportedOperationException("For Cluster Config Not Needed");
    }

    @Override
    public int getMaxActive() {
        return DEFAULT_MAX_ACTIVE_CONNECTION;
    }

    @Override
    public int getMaxIdle() {
        return DEFAULT_MAX_IDLE;
    }

    @Override
    public int getMinIdle() {
        return DEFAULT_MIN_IDLE;
    }

    @Override
    public int getMaxWaitInMillis() {
        return DEFAULT_MAX_WAIT_IN_MILLIS;
    }

    @Override
    public int getConnectionTimeOut() {
        return DEFAULT_CONNECTION_TIMEOUT;
    }

    @Override
    public int getReadTimeOut() {
        return DEFAULT_READ_TIMEOUT;
    }
}