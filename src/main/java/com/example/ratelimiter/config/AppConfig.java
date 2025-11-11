/*
package com.example.ratelimiter.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
@Configuration
public class AppConfig {
    @Value("${app.redis.host}")
    private String redisHost;
    @Value("${app.redis.port}")
    private int redisPort;
    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig cfg = new JedisPoolConfig();
        cfg.setMaxTotal(10);
        return new JedisPool(cfg, redisHost, redisPort);
    }
}
*/
