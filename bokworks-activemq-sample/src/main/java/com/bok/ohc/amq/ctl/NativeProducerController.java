package com.bok.ohc.amq.ctl;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.jms.Connection;
import jakarta.jms.DeliveryMode;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class NativeProducerController {
	
	@Autowired
	public ActiveMQConnectionFactory factory;
	
	@RequestMapping("/native/send")
	public String send(@RequestBody String body) {
		log.info("--- 네이티브 프로듀서 body [{}]", body);
		
		Connection con = null;
		Session session = null;
		
		try {
			con = factory.createConnection();
			session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("BOK.INBOUND.Q");
			MessageProducer producer = session.createProducer(queue);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			Message message = session.createTextMessage(body);
			producer.send(message);
			
			int index = 5;
			while ( index-- > 0 ) {
				log.info("--- SLEEP {}", index);
				Thread.sleep(1000);
			}
			
			session.commit();
			
		} catch ( Exception e ) {
			log.error(e.getMessage());
		} finally {
			try {
				
			} catch ( Exception e1 ) {
				log.error(e1.getMessage());
			}
		}
		
		
		log.info("--- 송신완료");
		return "송신 완료";
	}

}
