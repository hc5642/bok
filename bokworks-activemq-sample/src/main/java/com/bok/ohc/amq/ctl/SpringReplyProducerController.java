package com.bok.ohc.amq.ctl;

import java.util.UUID;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.jms.DeliveryMode;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SpringReplyProducerController {

	@Autowired
	public JmsTemplate jmsTemplate;

	@Autowired
	JmsMessagingTemplate jmsMessagingTemplate;

	@RequestMapping("/spring/send2reply")
	
	public String send2reply(@RequestBody String body) throws JMSException {
		jmsTemplate.setReceiveTimeout(1000L);
		jmsMessagingTemplate.setJmsTemplate(jmsTemplate);

		Session session = jmsMessagingTemplate.getConnectionFactory()
								.createConnection()
								.createSession(false,Session.AUTO_ACKNOWLEDGE);

		TextMessage message = session.createTextMessage(body);
		message.setJMSCorrelationID(UUID.randomUUID().toString());
		message.setJMSReplyTo(new ActiveMQQueue("BOK.REPLY.Q"));
		message.setJMSCorrelationID(UUID.randomUUID().toString());
		message.setJMSExpiration(1000L);
		message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		String reply = jmsMessagingTemplate.convertSendAndReceive(new ActiveMQQueue("BOK.INBOUND2.Q"), message, String.class);
		log.info("--- 메시지 송신 완료 [BOK.INBOUND2.Q][{}]", body);
		log.info("--- 메시지 송신 완료 응답수신 [BOK.REPLY.Q][{}]", reply);
		return reply;
	}

}
