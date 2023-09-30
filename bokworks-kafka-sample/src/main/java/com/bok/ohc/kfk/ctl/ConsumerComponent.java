package com.bok.ohc.kfk.ctl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 수신 확인 로그
 * 2023-10-01T08:46:20.223+09:00[0;39m [32m INFO[0;39m [35m9272[0;39m [2m---[0;39m [2m[ntainer#0-0-C-1][0;39m [36mcom.bok.ohc.kfk.ctl.ConsumerComponent   [0;39m [2m:[0;39m --- CONSUMER SUBSCRIBE MESSAGE [TEST+MESSAGE]
 * @author ohhyonchul
 *
 */
@Component
public class ConsumerComponent {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@KafkaListener(topics = "mopil", groupId = "group-id-mopil")
	public void receive(String input) {
		
		logger.info("--- CONSUMER SUBSCRIBE MESSAGE ["+input+"]");
		
	}

}
