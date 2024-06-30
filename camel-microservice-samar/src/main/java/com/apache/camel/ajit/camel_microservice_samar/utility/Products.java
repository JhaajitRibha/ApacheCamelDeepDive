package com.apache.camel.ajit.camel_microservice_samar.utility;

public class Products {

	private String name;
	private String life;
	private String price;
	private String expiry;
	
	public Products() {
		
		
	}

	public Products(String name, String life, String price, String expiry) {
		super();
		this.name = name;
		this.life = life;
		this.price = price;
		this.expiry = expiry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLife() {
		return life;
	}

	public void setLife(String life) {
		this.life = life;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	@Override
	public String toString() {
		return "Products [name=" + name + ", life=" + life + ", price=" + price + ", expiry=" + expiry + "]";
	}
	
	
}
