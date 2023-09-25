package com.zwash.auth.service;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducer<T> {

    private KafkaTemplate<String, T> kafkaTemplate;


   public KafkaProducer(KafkaTemplate<String, T> kafkaTemplate) {
	   this.kafkaTemplate=kafkaTemplate;
   }

    public void sendCarMessage(String topic, T message) {
  
        kafkaTemplate.send(topic, message);
    }
}
