package com.bluenetworks.webapp;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainerConfig {

	@Value("${fwaccess.properties.tomcat_ajp_port}")
    int ajpPort;
	
	@Value("${fwaccess.properties.tomcat_ajp_protocol}")
    String ajpProtocol;
	
	@Value("${fwaccess.properties.tomcat_ajp_enable}")
    boolean tomcatAjpEnabled;
	
	@Value("${fwaccess.properties.tomcat_ajp_address}")
	String ajpAddress;
	
	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(createAjpConnector());
		return tomcat;
	}
	
	public Connector createAjpConnector() {
		Connector ajpConnector = new Connector(ajpProtocol);
		ajpConnector.setPort(ajpPort);
        ajpConnector.setSecure(false);
        ajpConnector.setAllowTrace(false);
        ajpConnector.setScheme("http");
        ajpConnector.setAttribute("address", ajpAddress);
        ((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).setSecretRequired(false); 
        return ajpConnector;
        
	}
	
}
