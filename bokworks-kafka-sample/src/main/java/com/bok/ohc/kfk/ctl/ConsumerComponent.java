package com.bok.ohc.kfk.ctl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerComponent {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@KafkaListener(topics = "mopil", groupId = "group-id-mopil")
	public void receive(String input) {
		
		logger.info("--- CONSUMER SUBSCRIBE MESSAGE ["+input+"]");
		
	}

}
