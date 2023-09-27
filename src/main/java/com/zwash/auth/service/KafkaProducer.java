package com.zwash.auth.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.zwash.auth.exceptions.UserIsNotFoundException;
import com.zwash.common.pojos.User;



@Service
public class KafkaProducer<T> {

    private KafkaTemplate<String, T> kafkaTemplate;


   public KafkaProducer(KafkaTemplate<String, T> kafkaTemplate) {
	   this.kafkaTemplate=kafkaTemplate;
   }

    public void sendCarMessage(String topic, T message) {
  
        kafkaTemplate.send(topic, message);
    }
    public User getUser(long id) throws UserIsNotFoundException {
        // ... logic to retrieve user by ID
    	
//    	User user= userService.getUser(id);
//        GetUserResponse response = new GetUserResponse(user);
//        kafkaTemplate.send("get-user-topic", new GetUserRequest(id));
        return null;
    }
}
