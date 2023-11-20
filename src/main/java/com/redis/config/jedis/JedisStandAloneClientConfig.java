/* (C) Games24x7 */

package com.redis.config.jedis;

import com.redis.config.RedisClientConfig;

import java.util.List;

/**
 * Config which must be extended for using the StandAlong Configuration
 */
public abstract class JedisStandAloneClientConfig implements RedisClientConfig {

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public List<String> getClusterHostAndPort() {
        throw new UnsupportedOperationException("In StandAlone Mode Cluster support Not needed");
    }

    @Override
    public int getMaxRedirects() {
        throw new UnsupportedOperationException("In StandAlone Mode Cluster support Not needed");
    }

    @Override
    public String client() {
        return JEDIS_AS_CLIENT;
    }

    @Override
    public boolean isClusterEnabled() {
        return false;
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
