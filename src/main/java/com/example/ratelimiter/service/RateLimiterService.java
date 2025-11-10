package com.example.ratelimiter.service;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Service
public class RateLimiterService {
    private final JedisPool jedisPool;
    private String scriptSha;
    @Value("${app.ratelimit.limit}")
    private int limit;
    @Value("${app.ratelimit.windowSeconds}")
    private int windowSeconds;
    @Value("classpath:ratelimit.lua")
    private Resource luaScript;
    public RateLimiterService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    @PostConstruct
    public void loadScript() throws IOException {
        String script = new String(luaScript.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        try (Jedis j = jedisPool.getResource()) {
            scriptSha = j.scriptLoad(script);
            System.out.println("Loaded ratelimit.lua SHA: " + scriptSha);
        }
    }
    public Result checkAndIncrement(String key) {
        try (Jedis j = jedisPool.getResource()) {
            Object raw = j.evalsha(scriptSha, List.of(key), List.of(String.valueOf(limit), String.valueOf(windowSeconds)));
            if (raw instanceof List) {
                List<?> list = (List<?>) raw;
                long allowedFlag = ((Number) list.get(0)).longValue();
                long current = ((Number) list.get(1)).longValue();
                return new Result(allowedFlag == 1, current);
            } else {
                return new Result(false, -1);
            }
        }
    }
    public record Result(boolean allowed, long current) {}
}
