package com.redis.config.jedis;

public class JedisDefaultClientConfig extends JedisStandAloneClientConfig {

    private String host;

    private int port;
    public JedisDefaultClientConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }
}