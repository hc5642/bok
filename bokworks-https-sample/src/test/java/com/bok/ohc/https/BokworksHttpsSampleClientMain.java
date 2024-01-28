package com.bok.ohc.https;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class BokworksHttpsSampleClientMain {

	public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {

//		String httpsURL = "https://3.38.201.40:8084/hello";
		String httpsURL = "https://127.0.0.1:8084/hello";

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
				System.out.println("[checkClientTrusted]authType=" + authType);
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
				for ( X509Certificate cert : certs ) {
					System.out.println("[checkServerTrusted]getSigAlgName=" + cert.getSigAlgName());
					System.out.println("[checkServerTrusted]getSigAlgOID=" + cert.getSigAlgOID());
					System.out.println("[checkServerTrusted]getType=" + cert.getType());
					System.out.println("[checkServerTrusted]getVersion=" + cert.getVersion());
					System.out.println("[checkServerTrusted]getSignature=" + cert.getSignature());
				}
				System.out.println("[checkServerTrusted]authType=" + authType);
			}
		} };
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		URL url = new URL(httpsURL);

		// HttpsURLConnection 객체 생성
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

		// SSL 인증서 검증 비활성화 (테스트 목적으로만 사용)
		connection.setHostnameVerifier((hostname, session) -> true);

		// 요청 방식 설정 (GET, POST 등)
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Host", "3.38.201.40:8084");
		connection.setRequestProperty("Connection", "keep-alive");

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
		int count = 60;
		while ( count-- > 0 ) {
			System.out.println(count);
			Thread.sleep(1000);
		}
		
		connection.disconnect();

	}
}

