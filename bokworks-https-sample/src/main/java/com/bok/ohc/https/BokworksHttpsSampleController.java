package com.bok.ohc.https;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BokworksHttpsSampleController {
	
	@GetMapping("/hello")
	public String getHello() {
		return "Hello! HTTPS!!";
	}

}
