package com.apacheIntegration.integrationTwo.integrationTwo.routers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import com.apacheIntegration.integrationOne.integrationOne.entities.FileDynamics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Component
public class FileProcessor extends RouteBuilder{
	
	@Bean(name = "dataSource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/apacheCamelDatabase?useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

	@Override
	public void configure() throws Exception {
		from("activemq:queue:fileMessanger")
	    .log("Received message from ActiveMQ: ${body}")
	    .process((exchange)->{
	    	 String jsonBody = exchange.getIn().getBody(String.class);
             ObjectMapper mapper = new ObjectMapper();
             FileDynamics fileDynamics = mapper.readValue(jsonBody, FileDynamics.class);
             exchange.getIn().setBody(fileDynamics);
	    })
	    .log("received body : ${body}")
	    .log("id : ${body.id}")
	    .log("fjileName: ${body.fileName}")
	    .log("fileType : ${body.fileType}")
	    .log("fileContent :  ${body.fileContent}")
	    .setHeader("fileName",simple("${body.fileName}"))
	    .log("**********INTEGRATION_TWO***************")
	    .log("directing to databaseRetrivitionDirector :")
	    .log("---------------------------------------------")
	    .to("direct:databaseRetrivitionDirector");
		
		
		from("direct:databaseRetrivitionDirector")
        .setHeader("fileName", simple("${headers.fileName}"))
        .log("File name received from header: ${headers.fileName}")
        .to("sql:SELECT * FROM apachecameldatabase.file_dynamics WHERE file_name = :#fileName?dataSource=#dataSource")
        .log("query executed successfully")
        .process((exchange)->{
        	List<Map<String, Object>> resultList = exchange.getIn().getBody(List.class);
            if (resultList != null && !resultList.isEmpty()) {
                Map<String, Object> resultMap = resultList.get(0);
                exchange.getIn().setBody(resultMap);
            }
        	
            
        })
        .log("result received :: ${body}")
        .process((exchange)->{
        	 List<Map<String, Object>> resultList = exchange.getIn().getBody(List.class);
        	 if (resultList != null && !resultList.isEmpty()) {
                 Map<String, Object> resultMap = resultList.get(0);
                 FileDynamics fileDynamics = new FileDynamics();
                 fileDynamics.setId((Long) resultMap.get("id"));
                 fileDynamics.setFileName((String) resultMap.get("file_name"));
                 fileDynamics.setFileType((String) resultMap.get("file_type"));
                 fileDynamics.setFileContent((String) resultMap.get("file_content"));
                 exchange.getIn().setBody(fileDynamics);
             } else {
                 throw new RuntimeException("No data found for file: " + exchange.getIn().getHeader("fileName"));
             }
        })
        .log("Processed FileDynamics object from database: ${body}")
        .process((exchange)->{
        	FileDynamics fileDynamics = exchange.getIn().getBody(FileDynamics.class);
        	System.out.println(fileDynamics.getFileType().toLowerCase());

            String fileType = fileDynamics.getFileType().toLowerCase();
            
            String fileData = "";
            String fileExtension = fileType.toLowerCase();
            
            if ("json".equals(fileType)) {
                fileExtension = "json";
                ObjectMapper objectMapper = new ObjectMapper();
                fileData = objectMapper.writeValueAsString(fileDynamics);
            }else {
            	throw new Exception("xml file sent");
            }
            
            String processedFolder = "files/processedDatabase/";
            String timestamp = LocalDateTime.now().toString().replace(":", "-");
            String fileName = fileDynamics.getFileName().substring(0,3);
            

            String filePath = processedFolder + timestamp + fileName + "." + fileExtension;

            File processedFile = new File(filePath);
            try (PrintWriter writer = new PrintWriter(processedFile)) {
                writer.write(fileData);
            } catch (IOException e) {
                e.printStackTrace();
                
            }

            exchange.getIn().setBody("Operation completed for: " + fileName);
            		
        })
        .log("operation after database retrievition : ${body}")
        .log("uploading on sftp")
        .log("directing to sftp Uploader");
		
	    from("file:files/processedDatabase?noop=true") 
	    .log("File polled from inputDirectory: ${file:name}")
	    .to("sftp://localhost:2222/upload?username=john&password=RAW(p@ssw0rd)&strictHostKeyChecking=no&move=.complete&moveFailed=.failed")
	    .log("File uploaded successfully to SFTP server: ${file:name}")
	    .setHeader("fileName", simple("${file:name}"))
	    .to("activemq:queue:sftpMessanger")
	    .log("active mq sent with body : ${body} and file name : ${file:name}");

        

       
        
 
		
	}

}
