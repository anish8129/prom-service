/* (C) Games24x7 */
package com.redis.container.cluster;

import com.redis.config.lettuce.LettuceClusterClientConfig;

import java.util.List;

public class LettuceTestClusterConfig extends LettuceClusterClientConfig {

    private final List<String> hostAndPort;

    public LettuceTestClusterConfig(List<String> hostAndport) {
        this.hostAndPort = hostAndport;
    }

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
        return hostAndPort;
    }

    @Override
    public int getMaxRedirects() {
        return 2;
    }

    @Override
    public int getMaxActive() {
        return 4;
    }

    @Override
    public int getMaxIdle() {
        return 5;
    }

    @Override
    public int getMinIdle() {
        return 2;
    }

    @Override
    public int getMaxWaitInMillis() {
        return 5000;
    }

    @Override
    public int getConnectionTimeOut() {
        return 5000;
    }

    @Override
    public int getReadTimeOut() {
        return 5000;
    }
}
