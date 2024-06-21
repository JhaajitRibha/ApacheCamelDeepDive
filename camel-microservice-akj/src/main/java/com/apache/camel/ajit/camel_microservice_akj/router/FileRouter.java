package com.apache.camel.ajit.camel_microservice_akj.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("file:input?noop=true")
        .routeId("fileRoute")
        .log("Reading file: ${header.CamelFileName}")
        .log("File content: ${body}")
        .to("file:output");
	}

}
