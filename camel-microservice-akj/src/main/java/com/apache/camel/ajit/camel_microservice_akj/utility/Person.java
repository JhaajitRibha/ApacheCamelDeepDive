package com.apache.camel.ajit.camel_microservice_akj.utility;

public class Person {
	private int personId;
	private String personName;
	private String personCity;
	public Person(int personId, String personName, String personCity) {
		super();
		this.personId = personId;
		this.personName = personName;
		this.personCity = personCity;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPersonCity() {
		return personCity;
	}
	public void setPersonCity(String personCity) {
		this.personCity = personCity;
	}
	@Override
	public String toString() {
		return "Person [personId=" + personId + ", personName=" + personName + ", personCity=" + personCity + "]";
	}
	
	

}
