package com.bok.ohc.kfk.ctl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
	
	@Autowired
	public KafkaTemplate<String, String> kafkaTemplate;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/send")
	public String send(@RequestBody String input) {
		
		logger.info("--- PRODUCER PUBLISH MESSAGE ["+input+"]");
		kafkaTemplate.send("mopil", input);
		
		return "send ok";
	}


}
