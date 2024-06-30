package com.apacheIntegration.integrationOne.integrationOne.routers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FIlePickerTimer extends RouteBuilder {
    
    private static long timerCounter = 0;

    @Override
    public void configure() throws Exception {
    	from("timer://filePickerTimer?fixedRate=true&period=10000")
        .process(exchange -> {
            timerCounter++;
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            String message = "Triggered: " + timerCounter + " at: " + formattedDateTime;
            
            exchange.getMessage().setBody(message);
            exchange.getIn().setHeader("owner-name", "ajitKumarJha");
            exchange.setProperty("processed", true);
        })
        .log("****************************\n" +
             "File Picker Timer Triggered\n" +
             "Body: ${body}\n" +
             "***************************")
        .to("direct:filePicker");

    	from("direct:filePicker")
        .log("Checking for files in inputReader folder...")
        .pollEnrich("file:files/inputReader?delete=true&maxMessagesPerPoll=1")
        .process(this::decideFileType)
        .choice()
        .when(header("fileType").isEqualTo("xml"))
            .log("Processing XML file: ${file:name}")
            .to("file:files/xmlOutput")
            .setHeader("Content-Type", constant("application/xml"))
            .to("direct:xmlPostDirector")
            .log("File ${file:name} processed and moved to xmlOutput")
            .log("*******")
            .log("directed to xmlPoster")
        .when(header("fileType").isEqualTo("json"))
            .log("Processing JSON file: ${file:name}")
            .to("file:files/jsonOutput")
            .setHeader("Content-Type", constant("application/json"))
            .to("direct:jsonPostDirector")
            .log("File ${file:name} processed and moved to jsonOutput")
        .otherwise()
            .log("Unsupported file type or no files found. Waiting for the next trigger...");

        
    	from("direct:xmlPostDirector")
    	.setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .setBody(simple("${body}"))
        .to("http://localhost:8083/v1/api/files/saveData")
        .log("XML data posted to http://localhost:8083/v1/api/files/saveData")
        .log("HTTP Status: ${header.CamelHttpResponseCode}")
        .log("HTTP Response Body: ${body}");;
    	
    	from("direct:jsonPostDirector")
    	.setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .setBody(simple("${body}"))
        .to("http://localhost:8083/v1/api/files/saveData")
        .log("JSON data posted to http://localhost:8083/v1/api/files/saveData")
        .log("HTTP Status: ${header.CamelHttpResponseCode}")
        .log("HTTP Response Body: ${body}");;

    	
}
    
    private void decideFileType(Exchange exchange) {
      
        String fileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);

        if (fileName != null) {
            if (fileName.toLowerCase().endsWith(".xml")) {
                exchange.getMessage().setHeader("fileType", "xml");
            } else if (fileName.toLowerCase().endsWith(".json")) {
                exchange.getMessage().setHeader("fileType", "json");
            } else {
                exchange.getMessage().setHeader("fileType", "unsupported");
            }
        } else {
            exchange.getMessage().setHeader("fileType", "unsupported");
        }
        
        
        byte[] fileContentBytes = exchange.getIn().getBody(byte[].class);
        String fileContent = new String(fileContentBytes, StandardCharsets.UTF_8);
        exchange.getMessage().setBody(fileContent);
    }
}



