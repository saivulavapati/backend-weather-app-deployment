package com.weatherapp.whishlist_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class WhishlistServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhishlistServiceApplication.class, args);
	}

}
