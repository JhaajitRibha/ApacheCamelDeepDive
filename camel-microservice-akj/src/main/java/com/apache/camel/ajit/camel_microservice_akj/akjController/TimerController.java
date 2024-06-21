package com.apache.camel.ajit.camel_microservice_akj.akjController;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apache.camel.ajit.camel_microservice_akj.timer.TimerRouter;

@RestController
@RequestMapping("/rest/v1")
public class TimerController {
	
	@Autowired
	TimerRouter timerRouter;
	
	
	@PostMapping("/stopTimer")
	public void stopTimer() throws Exception{
		
		timerRouter.stopTimer();
		
	}

}