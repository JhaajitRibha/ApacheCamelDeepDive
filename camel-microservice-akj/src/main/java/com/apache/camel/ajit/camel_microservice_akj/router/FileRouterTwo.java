package com.apache.camel.ajit.camel_microservice_akj.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileRouterTwo extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("file:inputTwo?noop=true")
        .routeId("fileRouteTwo")
        .setHeader("creator",simple("${header.camelFileName}::ajitKumarJha"))
        .log("Reading file: ${header.CamelFileName}")
        .to("log:**************")
        .log("File content: ${body}")
        .to("activemq:queue:jsonMq")
        .onException(Exception.class)
        .log("Exception occurred: ${exception.message}")
        .handled(true)
        .to("log:error");;
		
	}

}
