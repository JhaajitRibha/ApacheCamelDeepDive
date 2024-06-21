package com.apache.camel.ajit.camel_microservice_akj.timer;




import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apache.camel.ajit.camel_microservice_akj.utility.Person;

import ch.qos.logback.classic.Logger;

@Component
public class TimerRouter extends RouteBuilder{
	
	@Autowired
	MessageProcessing messageProcessing;
	
	@Autowired
	Travellers travellers;

	@Override
	public void configure() throws Exception {
		from("timer:timerJee?period=5000&delay=5000&repeatCount=4")
		.routeId("timerFromAkj")
		.setBody().constant("hello" + " " + new Person(1, "ajit", "mumbai"))
		.to("log:hello-body")
		.transform().simple("time now is ${date:now:yyyy-MM-dd HH:mm:ss},original body was : ${body}")
		.to("log:timer-jee")
		.transform().simple("full body : ${body}")
		.to("log:full-body")
		.bean("messageProcessing","processMessage")
		.to("log:after bean")
		.bean(travellers, "listOfTravellers")
        .to("log:travellers");
		
		
		from("timer:timerKee?period=5000&delay=5000&repeatCount=4")
		.routeId("secondTimer")
		.setBody().constant("hello" + " " + new Person(1, "sahil", "mumbai"))
		.to("log:hello-bodySecond");
		

		
	}

	public void stopTimer() throws Exception {
		String routeid = "timerFromAkj";
		
	    
		String routeStatus = getContext().getRouteController().getRouteStatus(routeid).name();
		System.out.println("route status before stopping : " + routeStatus);
		
	
		getContext().getRouteController().stopRoute(routeid);
		
		routeStatus = getContext().getRouteController().getRouteStatus(routeid).name();
		System.out.println("route status after stopping : " + routeStatus);
		
	}
	
}

@Component
class MessageProcessing{
	public String processMessage(String body) {
		return ("processed message : "+ body);
	}
}

@Component
class Travellers{
	public List<String> listOfTravellers(){
		List<String> travellers =  new ArrayList<>();
		travellers.add("ajit");
		travellers.add("samar");
		
		return travellers;
		
	}
}
