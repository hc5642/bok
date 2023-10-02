package com.bok.ohc.amq.cfg;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import jakarta.jms.Session;

@Configuration
public class ActiveMQConfig {

  @Value("${spring.activemq.broker-url}")
  private String activemqBrokerUrl;

  @Value("${spring.activemq.user}")
  private String activemqUsername;

  @Value("${spring.activemq.password}")
  private String activemqPassword;
  
  @Bean
  public ActiveMQConnectionFactory activeMQConnectionFactory() {
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
    activeMQConnectionFactory.setBrokerURL(activemqBrokerUrl);
    activeMQConnectionFactory.setUserName(activemqUsername);
    activeMQConnectionFactory.setPassword(activemqPassword);
    return activeMQConnectionFactory;
  }
  
  @Bean
  public JmsTemplate jmsTemplate() {
    JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
    jmsTemplate.setExplicitQosEnabled(true);    // 메시지 전송 시 QOS을 설정
    jmsTemplate.setDeliveryPersistent(false);   // 메시지의 영속성을 설정
    jmsTemplate.setReceiveTimeout(1000 * 3);    // 메시지를 수신하는 동안의 대기 시간을 설정(3초)
    jmsTemplate.setTimeToLive(1000 * 60 * 30);  // 메시지의 유효 기간을 설정(30분)
    jmsTemplate.setSessionTransacted(false);
	jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
    return jmsTemplate;
  }
  
  @Bean
  public JmsListenerContainerFactory<?> jmsListenerContainerFactory() {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(activeMQConnectionFactory());
    factory.setSessionTransacted(false);
    factory.setTransactionManager(null);
    return factory;
  }
  
}