package com.example.ratelimiter.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class RateLimiterService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private String luaScriptText;
    private DefaultRedisScript<List> redisScript;

    @Value("${app.redis.rate-limit.limit}")
    private int limit;

    @Value("${app.redis.rate-limit.windowSeconds}")
    private int windowSeconds;

    @Value("classpath:ratelimit.lua")
    private Resource luaScript;

    /*public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }*/

    @PostConstruct
    public void loadScript() throws IOException {
        // Read Lua script from classpath
        luaScriptText = new String(luaScript.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaScriptText);
        redisScript.setResultType(List.class);
        System.out.println("Loaded ratelimit.lua into memory (Lettuce mode)");
    }

    public Result checkAndIncrement(String key) {
        // Execute Lua atomically on the correct cluster node
        List<?> raw = redisTemplate.execute(redisScript,
                List.of(key),
                String.valueOf(limit),
                String.valueOf(windowSeconds));

        if (raw != null && raw.size() >= 2) {
            long allowedFlag = ((Number) raw.get(0)).longValue();
            long current = ((Number) raw.get(1)).longValue();
            return new Result(allowedFlag == 1, current);
        }
        return new Result(false, -1);
    }

    public record Result(boolean allowed, long current) {}
}
