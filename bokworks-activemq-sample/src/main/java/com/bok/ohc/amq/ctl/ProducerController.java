package com.bok.ohc.amq.ctl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ProducerController {
	
	@Autowired
	public JmsTemplate jmsTemplate;
	
	@RequestMapping("/send")
	public String send(@RequestBody String body) {
		log.info("--- 세션 트랜잭티드 설정 [{}]", jmsTemplate.isSessionTransacted());
		jmsTemplate.convertAndSend("BOK.INBOUND.Q", body);
		try {
			int index = 5;
			while ( index-- > 0 ) {
				log.info("--- SLEEP {}", index);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "송신 완료";
	}

}
