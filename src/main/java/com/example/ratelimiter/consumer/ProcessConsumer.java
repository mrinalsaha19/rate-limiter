package com.example.ratelimiter.consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
public class ProcessConsumer {
    @KafkaListener(topics = "${app.kafka.topic.process}", groupId = "process-handler")
    public void onProcess(ConsumerRecord<String, String> record) {
        System.out.println("PROCESS consumer received key=" + record.key() + " value=" + record.value());
    }
}
