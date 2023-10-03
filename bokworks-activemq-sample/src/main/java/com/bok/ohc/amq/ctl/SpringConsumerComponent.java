package com.bok.ohc.amq.ctl;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SpringConsumerComponent implements SessionAwareMessageListener<Message> {
	
	@JmsListener(destination="BOK.INBOUND.Q")
	public void receive(String input) {
		log.info("--- Jms 리스너 메시지 수신 [{}]", input);
	}
	
	@Override
	@JmsListener(destination = "BOK.INBOUND3.Q")
	public void onMessage(Message message, Session session) {
		
		try {
			
			String body = message.getBody(String.class);
			log.info("--- [CONSUMER] 메시지 수신 [BOK.INBOUND3.Q][{}]", body);
			
	        // done handling the request, now create a response message
	        ObjectMessage responseMessage = new ActiveMQObjectMessage();
	        responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
	        
	        try {
	        	log.info("--- [CONSUMER] 나름의 비즈니스 로직 처리중 ...");
				Thread.sleep(4*1000);
				log.info("--- [CONSUMER] 나름의 비즈니스 로직 처리완료");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        
	        responseMessage.setObject("BIZ RESPONSE MESSAGE!!");
	
	        // Message sent back to the replyTo address of the income message.
	        MessageProducer producer = session.createProducer(message.getJMSReplyTo());
	        producer.send(responseMessage);
	        log.info("--- [CONSUMER] 응답 송출완료. [message.getJMSReplyTo()]");
		} catch ( JMSException e ) {
			log.error("--- [CONSUMER] 에러발생 [{}]", e.getMessage());
		}
		
	}
	

}
