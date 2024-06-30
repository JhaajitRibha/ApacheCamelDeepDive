package com.apache.camel.ajit.camel_microservice_akj.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SftpUploader extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
		 from("file:inputDirectory?noop=true")
         .routeId("fileToSftpRoute")
         .to("sftp://localhost:2222/upload?username=john&password=RAW(p@ssw0rd)&strictHostKeyChecking=no&move=.complete&moveFailed=.failed")
         .log("File uploaded successfully to SFTP server: ${file:name}");
		
	}

}
