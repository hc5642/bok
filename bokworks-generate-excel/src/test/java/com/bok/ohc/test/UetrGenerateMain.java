package com.bok.ohc.test;

import java.util.UUID;

public class UetrGenerateMain {

	public static void main(String[] args) {
		
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString();
		System.out.println(uuidStr);

	}

}
