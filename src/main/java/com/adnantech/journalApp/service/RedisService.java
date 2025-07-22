package com.adnantech.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> weatherResponse) {
        try {
            log.info("Getting response in redis with key {}", key);
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), weatherResponse);
        } catch (Exception e) {
            log.error("Redis Get Exception {}", e.getLocalizedMessage(), e);
            return null;
        }
    }

    public void set(String key, Object o, Long ttl) {
        try {
            log.info("Setting response in redis with key {}", key);
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis Set Exception {}", e.getLocalizedMessage(), e);
        }
    }

}
