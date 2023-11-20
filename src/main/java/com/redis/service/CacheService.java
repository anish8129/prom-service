/* (C) Games24x7 */

package com.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CacheService {

    private final RedisTemplate<String, String> redisTemplate;

    public CacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, int ttlInSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlInSeconds));
    }

    public void setIfAbsent(String key,String value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public void setIfAbsent(String key, String value, int ttlInSeconds) {
        redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(ttlInSeconds));
    }

    public <T> Optional<T> get(String key, Class<T> type) {
        Object o = redisTemplate.opsForValue().get(key);
        T t = typeCheck(type, o) ? type.cast(o) : null;
        return Optional.ofNullable(t);
    }

    public <T> T executeScript(RedisScript<T> script, List<String> keys, Object... args) {
        return redisTemplate.execute(script, keys, args);
    }

    private static boolean typeCheck(Class<?> requiredType, @Nullable Object candidate) {
        return candidate == null || ClassUtils.isAssignable(requiredType, candidate.getClass());
    }

    public void putAll(String key, Map<Object, Object> objectObjectMap) {
        redisTemplate.opsForHash().putAll(key, objectObjectMap);
    }

    public Map<Object, Object> getAllMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
