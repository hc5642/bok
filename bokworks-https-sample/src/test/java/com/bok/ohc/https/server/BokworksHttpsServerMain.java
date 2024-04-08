package com.bok.ohc.https.simple;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

/**
 * https://www.delftstack.com/howto/java/java-https-server/
 * keytool -genkeypair -keyalg RSA -alias selfsigned -keystore testkey.jks -storepass password -validity 360 -keysize 2048
 * @author ohhyonchul
 *
 */
public class HttpServerMain {
	public static class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String Response = "<BOKWireResponse>Success</BOKWireResponse>";
			InputStream in = exchange.getRequestBody();
			byte [] inputByte = in.readAllBytes();
			System.out.println(new String(inputByte));
//			exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
			exchange.getResponseHeaders().add("Connection", "Keep-Alive");
			exchange.getResponseHeaders().add("Keep-Alive", "timeout=14");
			exchange.getResponseHeaders().add("Content-Type", "text/xml");
			exchange.sendResponseHeaders(200, Response.getBytes().length);
			OutputStream Output_Stream = exchange.getResponseBody();
			Output_Stream.write(Response.getBytes());
			Output_Stream.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		try {
			// initialize the HTTPS server
			HttpsServer server = HttpsServer.create(new InetSocketAddress(9000), 0);
			server.setHttpsConfigurator(new HttpsConfigurator(getSslConfiguration()) {
				public void configure(HttpsParameters params) {
					try {
						// initialise the SSL context
						SSLContext SSL_Context = getSSLContext();
						SSLEngine SSL_Engine = SSL_Context.createSSLEngine();
						params.setNeedClientAuth(false);
						params.setCipherSuites(SSL_Engine.getEnabledCipherSuites());
						params.setProtocols(SSL_Engine.getEnabledProtocols());

						// Set the SSL parameters
						SSLParameters SSL_Parameters = SSL_Context.getSupportedSSLParameters();
						params.setSSLParameters(SSL_Parameters);
						System.out.println("The HTTPS server is connected");

					} catch (Exception ex) {
						System.out.println("Failed to create the HTTPS port");
					}
				}
			});
			server.createContext("/test", new MyHandler());
			server.setExecutor(null); // creates a default executor
			server.start();

		} catch (Exception exception) {
			System.out.println("Failed to create HTTPS server on port " + 9000 + " of localhost");
			exception.printStackTrace();
		}
	}
	
	public static SSLContext getSslConfiguration() {
		
		SSLContext ssl = null;
		try {
			ssl = SSLContext.getInstance("TLS");
			// initialise the keystore
			char[] Password = "password".toCharArray();
			KeyStore keyStore = KeyStore.getInstance("JKS");
			FileInputStream is = new FileInputStream("C:\\Users\\ohhyonchul\\Documents\\testkey.jks");
			keyStore.load(is, Password);

			// setup the key manager factory
			KeyManagerFactory keyManager = KeyManagerFactory.getInstance("SunX509");
			keyManager.init(keyStore, Password);

			// setup the trust manager factory
			TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
			trustManager.init(keyStore);

			// setup the HTTPS context and parameters
			ssl.init(keyManager.getKeyManagers(), trustManager.getTrustManagers(), null);
			
		} catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		
		return ssl;
	}


}