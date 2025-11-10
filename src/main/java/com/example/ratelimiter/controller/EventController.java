package com.example.ratelimiter.controller;
import com.example.ratelimiter.service.KafkaProducerService;
import com.example.ratelimiter.service.RateLimiterService;
import com.example.ratelimiter.service.RateLimiterService.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/events")
public class EventController {
    private final RateLimiterService rateLimiter;
    private final KafkaProducerService kafkaProducer;
    public EventController(RateLimiterService rateLimiter, KafkaProducerService kafkaProducer) {
        this.rateLimiter = rateLimiter;
        this.kafkaProducer = kafkaProducer;
    }
    @PostMapping
    public ResponseEntity<?> postEvent(@RequestParam("key") String key, @RequestBody String payload) {
        Result r = rateLimiter.checkAndIncrement("rl:" + key);
        if (r.allowed()) {
            kafkaProducer.sendToProcess(key, payload);
            return ResponseEntity.accepted().body("accepted;count=" + r.current());
        } else {
            kafkaProducer.sendToDeferred(key, payload);
            return ResponseEntity.status(429).body("deferred;count=" + r.current());
        }
    }
}
