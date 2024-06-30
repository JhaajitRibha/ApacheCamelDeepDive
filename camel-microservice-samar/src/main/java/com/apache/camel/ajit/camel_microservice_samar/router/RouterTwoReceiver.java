package com.apache.camel.ajit.camel_microservice_samar.router;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.apache.camel.ajit.camel_microservice_samar.utility.Products;

//from("file:input?noop=true")
//.routeId("fileRouteTwo")
//.setHeader("creator",simple("${header.camelFileName}::ajitKumarJha"))
//.log("Reading file: ${header.CamelFileName}")
//.log("File content: ${body}")
//.to("activemq:queue:jsonMq")
//.onException(Exception.class)
//.log("Exception occurred: ${exception.message}")
//.handled(true)
//.to("log:error");;


@Component
public class RouterTwoReceiver extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("activemq:queue:jsonMq")
		.unmarshal().json(JsonLibrary.Jackson,Products.class)
		.log("${body}")
		.process(exchange->{
			System.out.println("Exchange: " + exchange);
			System.out.println("**************************");
			
			 Products products = exchange.getIn().getBody(Products.class);

		        String creator = exchange.getIn().getHeader("creator", String.class);
		        String fileName = exchange.getIn().getHeader("fileName", String.class);

		        System.out.println("Received JSON body: " + products);
		        System.out.println("Creator: " + creator);
		        System.out.println("File Name: " + fileName);
		        
		        System.out.println("******************");
		        System.out.println("****END****");	
		})
		.marshal().json()
		.to("file:files/outputTwo")
		.log("File stored successfully to files/outputTwo");;
		
		
	}

}
