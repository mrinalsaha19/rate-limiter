package com.example.ratelimiter.controller;
import com.example.ratelimiter.service.KafkaProducerService;
import com.example.ratelimiter.service.RateLimiterService;
import com.example.ratelimiter.service.RateLimiterService.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/events")
public class EventController {
    private final RateLimiterService rateLimiter;
    private final KafkaProducerService kafkaProducer;

    @Value("${app.redis.rate-limit.key}")
    private String rateLimiterKey;
    public EventController(RateLimiterService rateLimiter, KafkaProducerService kafkaProducer) {
        this.rateLimiter = rateLimiter;
        this.kafkaProducer = kafkaProducer;
    }
    @PostMapping
    public ResponseEntity<?> postEvent(@RequestBody String payload) {
        Result r = rateLimiter.checkAndIncrement(rateLimiterKey);
        if (r.allowed()) {
            // Replace key for kafka producers as per partition requirement.
            kafkaProducer.sendToProcess(rateLimiterKey, payload);
            return ResponseEntity.accepted().body("accepted;count=" + r.current());
        } else {
            kafkaProducer.sendToDeferred(rateLimiterKey, payload);
            return ResponseEntity.status(429).body("deferred;count=" + r.current());
        }
    }
}
