package com.bok.ohc.https;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BokworksHttpsSampleController {
	
	@GetMapping("/hello")
	public String getHello() {
		return "Hello! HTTPS!!";
	}
	
	/*
	 * 스프링부트에 HTTPS 설정하기
	 * https://velog.io/@cho876/Springboot-httpsSSL-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0
	 */

}
