package com.bok.ohc.amq.ctl;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsumerComponent {
	
	@JmsListener(destination="BOK.INBOUND.Q")
	public void receive(String input) {
		log.info("--- Jms 리스너 메시지 수신 [{}]", input);
	}

}
