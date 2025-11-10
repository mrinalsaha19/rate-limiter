package com.example.ratelimiter.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${app.kafka.topic.process}")
    private String processTopic;
    @Value("${app.kafka.topic.deferred}")
    private String deferredTopic;
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendToProcess(String key, String payload) {
        kafkaTemplate.send(processTopic, key, payload);
    }
    public void sendToDeferred(String key, String payload) {
        kafkaTemplate.send(deferredTopic, key, payload);
    }
}
