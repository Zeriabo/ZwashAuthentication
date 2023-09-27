package com.zwash.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.zwash.common.pojos.Car;

@Service
public class MessageProducer {

	private final KafkaTemplate<String, Object> carKafkaTemplate;
	private final KafkaTemplate<String, Object> userKafkaTemplate;

	@Autowired
	public MessageProducer(KafkaTemplate<String, Object> carKafkaTemplate,
			KafkaTemplate<String, Object> userKafkaTemplate) {
		this.carKafkaTemplate = carKafkaTemplate;
		this.userKafkaTemplate = userKafkaTemplate;
	}

	public void sendCarMessage(Car car) {
		// Send Car-related message using the carKafkaTemplate
		carKafkaTemplate.send("car-topic", car);
	}



	// Add similar methods for other message types (Booking, etc.)
}
