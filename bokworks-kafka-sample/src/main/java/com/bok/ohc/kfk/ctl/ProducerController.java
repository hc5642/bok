package com.bok.ohc.kfk.ctl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * (송신 커맨드) C:\Users\ohhyonchul>curl http://localhost:8080/send -d "TEST MESSAGE"
 * @author ohhyonchul
 *
 */
@Slf4j
@RestController
public class ProducerController {
	
	@Autowired
	public KafkaTemplate<String, String> kafkaTemplate;
	
	@RequestMapping("/send")
	public String send(@RequestBody String input) {
		
		log.info("--- PRODUCER PUBLISH MESSAGE [{}]", input);
		kafkaTemplate.send("mopil", input);
		
		return "send ok";
	}


}
