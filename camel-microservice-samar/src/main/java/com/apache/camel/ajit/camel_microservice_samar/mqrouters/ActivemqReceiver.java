package com.apache.camel.ajit.camel_microservice_samar.mqrouters;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ActivemqReceiver extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		 from("activemq:mqMicroserviceAkj")
         .to("log:received message from activemq");
		
	}

}
