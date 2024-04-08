package com.bok.ohc.https;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BokworksHttpSampleClientMain {

	public static void main(String[] args) {

		String httpsURL = "http://3.38.201.40:61616/";
		URL url;
		HttpURLConnection connection = null;
		
		try {
			
			url = new URL(httpsURL);
			connection = (HttpURLConnection) url.openConnection();
		
			// 요청 방식 설정 (GET, POST 등)
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Host", "3.38.201.40:61616");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Keep-Alive", "timeout=40");
	
			// 응답 코드 확인
			int responseCode = connection.getResponseCode();
			System.out.println("Response Code: " + responseCode);
			System.out.println("[Response Header] Keep-alive=" + connection.getHeaderField("Keep-alive"));
	
			// 응답 내용 읽기
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
	
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
	
			// 응답 내용 출력
			System.out.println("Response: " + response.toString());
	
			// 연결 종료
			int count = 70;
			while ( count-- > 0 ) {
				System.out.println(count);
				Thread.sleep(1000);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if ( connection != null ) 
					connection.disconnect();
			} catch ( Exception e2 ) {}
		}

	}
}
