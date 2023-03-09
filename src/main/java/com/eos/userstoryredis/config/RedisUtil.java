package com.eos.userstoryredis.config;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getRandomKey() {
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            byte[] randomKeyBytes = connection.randomKey();
            if (randomKeyBytes != null) {
                return new String(randomKeyBytes);
            }
            return null;
        });
    }
}

