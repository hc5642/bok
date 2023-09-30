package com.bok.ohc.kfk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * (사전준비1) Zookiker 실행명령어 
 *     PS C:\dev\kafka_2.12-3.5.1> .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
 * (사전준비2) Kafka 브로커 실행 명령어
 *     PS C:\dev\kafka_2.12-3.5.1> .\bin\windows\kafka-server-start.bat .\config\server.properties
 * (사전준비3) 리스닝 상태 확인 명령어
 *     C:\Users\ohhyonchul>netstat -a | findstr -i "2181 9092"
 * @author ohhyonchul
 *
 */
@SpringBootApplication
public class BokworksKafkaSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BokworksKafkaSampleApplication.class, args);
	}

}
