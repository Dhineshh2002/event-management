package com.example.eventmanager.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.eventmanager.util.Utils.validateCacheKey;

@Component
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        validateCacheKey(key);
        Objects.requireNonNull(value, "value must not be null");
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout must be positive");
        }
        redisTemplate.opsForValue().set(
                key,
                value,
                timeout,
                unit
        );
    }

    public void set(String key, Object value) {
        validateCacheKey(key);
        Objects.requireNonNull(value, "value must not be null");
        redisTemplate.opsForValue().set(
                key,
                value
        );
    }

    public <T> T get(String key, Class<T> clazz) {
        validateCacheKey(key);
        Objects.requireNonNull(clazz, "clazz must not be null");
        Object val = redisTemplate.opsForValue().get(key);
        if (val == null) {
            return null;
        }
        return clazz.cast(val);
    }

    public void delete(List<String> keys) {
        Objects.requireNonNull(keys, "keys must not be null");
        if (keys.isEmpty()) return;
        redisTemplate.delete(keys);
    }

    public boolean exists(String key) {
        validateCacheKey(key);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void increment(String key) {
        validateCacheKey(key);
        redisTemplate.opsForValue().increment(key);
    }
}
