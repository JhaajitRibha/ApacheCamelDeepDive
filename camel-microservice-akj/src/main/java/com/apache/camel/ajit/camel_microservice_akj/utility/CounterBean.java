package com.apache.camel.ajit.camel_microservice_akj.utility;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class CounterBean {
	
	private final AtomicInteger counter = new AtomicInteger(0);
	
	public int incrementAndGet() {
		return counter.incrementAndGet();
	}

}
