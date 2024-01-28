package com.bok.ohc.https;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BokworksHttpsSampleConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		factory.addConnectorCustomizers(connector -> {
			AbstractHttp11Protocol protocol = (AbstractHttp11Protocol) connector.getProtocolHandler();

			int originMaxKeepAliveRequests = protocol.getMaxKeepAliveRequests();
			protocol.setMaxKeepAliveRequests(-1);
			int originKeepAliveTimeout = protocol.getKeepAliveTimeout();
			protocol.setKeepAliveTimeout(15000);

			logger.info("####################################################################################");
			logger.info("#");
			logger.info("# TomcatCustomizer");
			logger.info("#");
			logger.info("# origin maxKeepAliveRequests {}", originMaxKeepAliveRequests);
			logger.info("# custom maxKeepAliveRequests {}", protocol.getMaxKeepAliveRequests());
			logger.info("# origin keepalive timeout: {} ms", originKeepAliveTimeout);
			logger.info("# keepalive timeout: {} ms", protocol.getKeepAliveTimeout());
			logger.info("# connection timeout: {} ms", protocol.getConnectionTimeout());
			logger.info("# max connections: {}", protocol.getMaxConnections());
			logger.info("#");
			logger.info("####################################################################################");

		});
	}
}