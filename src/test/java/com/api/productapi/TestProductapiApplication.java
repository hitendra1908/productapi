package com.api.productapi;

import org.springframework.boot.SpringApplication;

public class TestProductapiApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProductapiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
