package com.weatherapp.weather_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.cloud.config.enabled=false",
		"eureka.client.enabled=false"
})
class WeatherServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
