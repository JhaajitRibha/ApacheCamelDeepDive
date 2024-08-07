package com.apache.camel.ajit.camel_microservice_akj.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DirectRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("direct:start")
      .log("Before processing: ${body}")
      .setBody(constant("Hello from start endpoint"))
      .to("direct:process")
      .log("After processing: ${body}");

       from("direct:process")
      .log("Processing message: ${body}")
      .process(exchange -> {
          String body = exchange.getIn().getBody(String.class);
          String processedBody = "Processed: " + body;
          exchange.getIn().setBody(processedBody);
      });
		
	}

}
