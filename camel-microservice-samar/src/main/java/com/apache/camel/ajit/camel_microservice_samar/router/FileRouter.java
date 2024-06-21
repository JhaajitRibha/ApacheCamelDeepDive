package com.apache.camel.ajit.camel_microservice_samar.router;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.apache.camel.ajit.camel_microservice_samar.utility.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FileRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("file:files/input?fileName=studentFive.txt&delay=180000")
	    .routeId("fileRoute")
	    .unmarshal().json(JsonLibrary.Jackson, Student.class)
	    .log("Reading file: ${header.CamelFileName}")
	    .process(exchange -> {
	        Student student = exchange.getIn().getBody(Student.class);
	        System.out.println("File content via processor: " + student);
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        String studentJson = objectMapper.writeValueAsString(student);
	        
	        exchange.getIn().setBody(studentJson);
	    })
	    .log("File content after processor: ${body}")
	    .to("file:files/output");


        
	}

}
