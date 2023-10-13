package com.xyongfeng.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    public final static String KEY_ONLINE = "keyOnline";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void put(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object get(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public void delete(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);

    }


    public void putUserOnline(Integer userId, String token) {
        put(KEY_ONLINE, userId.toString(), token);
    }

    public void deleteUserOnline(Integer userId) {
        delete(KEY_ONLINE, String.valueOf(userId));
    }

    public String getUserOnline(Integer userId) {
        return (String) get(KEY_ONLINE, userId.toString());
    }

}
