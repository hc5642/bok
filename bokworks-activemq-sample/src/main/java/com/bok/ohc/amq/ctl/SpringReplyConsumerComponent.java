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
public class SpringReplyConsumerComponent implements SessionAwareMessageListener<Message> {
	
	@Override
	@JmsListener(destination = "BOK.INBOUND2.Q")
	public void onMessage(Message message, Session session) throws JMSException {
		
		String body = message.getBody(String.class);
		log.info("--- 메시지 수신 [BOK.INBOUND2.Q][{}]", body);
		
        // done handling the request, now create a response message
        ObjectMessage responseMessage = new ActiveMQObjectMessage();
        responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
        responseMessage.setObject("receive ok!");

        // Message sent back to the replyTo address of the income message.
        MessageProducer producer = session.createProducer(message.getJMSReplyTo());
        producer.send(responseMessage);
        log.info("--- 메시지 수신 응답 송출완료. [message.getJMSReplyTo()]");
		
	}
}
