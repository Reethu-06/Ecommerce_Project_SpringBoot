package com.newOne.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
public class RedisConfig {

    // Bean to configure and return a StringRedisTemplate
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // StringRedisTemplate is used to interact with Redis when storing and retrieving strings
        return new StringRedisTemplate(redisConnectionFactory);
    }

    // Bean to configure and return ValueOperations for String keys and values in Redis
    @Bean
    public ValueOperations<String, String> valueOperations(StringRedisTemplate template) {
        // ValueOperations is used for performing Redis operations like set, get, etc., on string values
        return template.opsForValue();
    }
}
