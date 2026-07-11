package com.CharlesRiverDevelopment.loan_management_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<SendResult<String, String>> publish(
            String topic,
            String key,
            String payload
    ) {

        System.out.print("KafkaProducer.publish() called with topic: " + topic + ", key: " + key + ", payload: " + payload);

        return kafkaTemplate.send(topic, key, payload);
    }
}
