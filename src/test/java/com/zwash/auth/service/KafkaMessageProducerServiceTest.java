package com.zwash.auth.service;


import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

public class KafkaMessageProducerServiceTest {

    @Test
    void testSendMessage() {
        // Create a mock KafkaTemplate
        KafkaTemplate<String, Object> kafkaTemplate = mock(KafkaTemplate.class);

        // Create an instance of KafkaMessageProducerService with the mock KafkaTemplate
        KafkaMessageProducerService producerService = new KafkaMessageProducerService(kafkaTemplate);

        // Define test inputs
        String topic = "test-topic";
        String key = "test-key";
        String message = "test-message";

        // Call the sendMessage method
        producerService.sendMessage(topic, key, message);

        // Verify that the KafkaTemplate's send method was called with the expected arguments
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(key), eq(message));

        // Optionally, you can add more assertions or verifications based on your use case
    }
}
