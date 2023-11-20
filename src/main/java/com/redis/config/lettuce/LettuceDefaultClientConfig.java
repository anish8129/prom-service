/* (C) Games24x7 */

package com.redis.config.lettuce;

public class LettuceDefaultClientConfig extends LettuceStandAloneClientConfig {
    private String host;

    private int port;
    public LettuceDefaultClientConfig(String host, int port) {
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
