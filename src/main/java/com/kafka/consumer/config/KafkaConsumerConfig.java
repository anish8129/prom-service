/* (C) Games24x7 */
package com.kafka.consumer.config;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Collection;
import java.util.Map;

public interface KafkaConsumerConfig {
    String getBootStrapServers();

    String getGroupId();

    <K> Deserializer<K> getKeySerializer();

    <V> Deserializer<V> getValueSerializer();

    Collection<String> getTopic();

    boolean isEnabled();

    Boolean isAutoCommitEnabled();

    int threadCount();

    String[] getAdditionalProperties();

    String getName();

    Boolean useCustomRebalance();
}
