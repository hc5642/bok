package com.bok.ohc.amq.ctl;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SpringProducerController {

	@Autowired
	public JmsTemplate jmsTemplate;

	@Autowired
	JmsMessagingTemplate jmsMessagingTemplate;

	/**
	 * 별도의 수취응답 없이 단순 송신만 하는 경우
	 * @param body
	 * @return
	 */
	@RequestMapping("/spring/send")
	public String send(@RequestBody String body) {
		log.info("--- 세션 트랜잭티드 설정 [{}]", jmsTemplate.isSessionTransacted());
		jmsTemplate.convertAndSend("BOK.INBOUND.Q", body);
		return "송신 완료";
	}

	/**
	 * 컨슈머로부터 수취확인 메시지를 수신하는 경우
	 * @param body
	 * @return
	 * @throws JMSException
	 */
	@RequestMapping("/spring/send2")
	public String send2(@RequestBody String body) {
		
		String reply = null;
		
		try {
			Message message = jmsTemplate.sendAndReceive("BOK.INBOUND3.Q", new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					Message message = session.createTextMessage(body);
					message.setJMSReplyTo(new ActiveMQQueue("BOK.REPLY2.Q"));
					message.setJMSRedelivered(true);
					return message;
				}
			});
			log.info("--- [PRODUCER] 송신 완료 [{}]", body);
			reply = message.getBody(String.class);
		} catch (Exception e) {
			log.error("--- [PRODUCER] 응답수신에러 [{}]", e.getMessage());
		}
		log.info("--- [PRODUCER] 응답수신 완료 [{}]", reply);
		
		return reply; 
	}

}
