/* (C) Games24x7 */
package com.redis.container.cluster;

import com.redis.config.jedis.JedisClusterClientConfig;

import java.util.List;

public class JedisTestClusterConfig extends JedisClusterClientConfig {

    private final List<String> hostAndPort;

    public JedisTestClusterConfig(List<String> hostAndport) {
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
}
