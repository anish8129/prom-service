/* (C) Games24x7 */
package com.funnel;

public class EventAttribute {
    String attribute;
    String keyname;
    int cache;
    String cacheprefix;
    String api;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public int getCache() {
        return cache;
    }

    public void setCache(int cache) {
        this.cache = cache;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getCacheprefix() {
        return cacheprefix;
    }

    public void setCacheprefix(String cacheprefix) {
        this.cacheprefix = cacheprefix;
    }

    @Override
    public String toString() {
        return "EventAttribute{" +
                "attribute='" + attribute + '\'' +
                ", keyname='" + keyname + '\'' +
                ", cache=" + cache +
                ", cacheprefix='" + cacheprefix + '\'' +
                ", api='" + api + '\'' +
                '}';
    }
}
