package com.apache.camel.ajit.camel_microservice_akj.activemqrouters;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apache.camel.ajit.camel_microservice_akj.utility.CounterBean;

@Component
public class ActiveMqSender extends RouteBuilder{

	@Autowired
	private CounterBean counterBean;
	
	@Override
	public void configure() throws Exception {
//	    from("timer:activemq-timer?period=10000")
//	    .transform().constant("message from active mq server one",counter++)
//	    .to("activemq:mqMicroserviceAkj");
		
	    from("timer:activemq=timer?period=60000")
	    .setBody().simple("message from akj: ,counter: ${bean:counterBean?method=incrementAndGet}")
	    .to("activemq:mqMicroserviceAkj");
		
	}
	

}
