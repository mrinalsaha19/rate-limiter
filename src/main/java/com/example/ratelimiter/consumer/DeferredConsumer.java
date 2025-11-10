package com.example.ratelimiter.consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
public class DeferredConsumer {
    @KafkaListener(topics = "${app.kafka.topic.deferred}", groupId = "deferred-handler")
    public void onDeferred(ConsumerRecord<String, String> record) {
        System.out.println("DEFERRED consumer received key=" + record.key() + " value=" + record.value());
    }
}
