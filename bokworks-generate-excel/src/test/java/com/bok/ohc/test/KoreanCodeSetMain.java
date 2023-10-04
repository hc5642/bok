package com.bok.ohc.test;

import java.io.UnsupportedEncodingException;

public class KoreanCodeSetMain {

	public static void main(String[] args) {
		String recvText = "무궁화 꽃이 피었습니다.";

		String[] charSet = { "utf-8", "ksc5601" }; //"iso-8859-1", "x-windows-949"

		for (int i = 0; i < charSet.length; i++) {
			for (int j = 0; j < charSet.length; j++) {
				try {
					System.out.println(
							"[" + charSet[i] + " > " + charSet[j] + "] : "
							+ new String(recvText.getBytes(charSet[i]), charSet[j]));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
