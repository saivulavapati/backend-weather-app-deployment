package com.weatherapp.weather_service.service;

import com.weatherapp.weather_service.client.WeatherClient;
import com.weatherapp.weather_service.dto.WeatherResponse;
import com.weatherapp.weather_service.exception.BadRequestException;
import com.weatherapp.weather_service.exception.InvalidCoordinatesException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Mock
    private WeatherClient weatherClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(weatherService, "apiKey", "dummy-api-key");
    }

    @Test
    void testGetCityWeatherDetails_Success() {
        String mockResponse = "{\"temp\":25,\"description\":\"Sunny\"}";
        when(weatherClient.getWeather("London", "dummy-api-key", "metric")).thenReturn(mockResponse);
        WeatherResponse response = weatherService.getCityWeatherDetails("London");
        assertNotNull(response);
        assertEquals(mockResponse, response.getData());
    }

    @Test
    void testGetCityWeatherDetails_EmptyCity() {
        Exception exception = assertThrows(BadRequestException.class, () -> weatherService.getCityWeatherDetails(""));
        assertEquals("City cannot be empty.", exception.getMessage());
    }

    @Test
    void testGetCityWeatherDetails_FeignException() {
        when(weatherClient.getWeather("London", "dummy-api-key", "metric")).thenThrow(FeignException.class);
        assertThrows(FeignException.class, () -> weatherService.getCityWeatherDetails("London"));
    }

    @Test
    void testGetHourlyForecast_Success() {
        String mockResponse = "{\"hourly\":[{\"temp\":20},{\"temp\":22}]}";
        when(weatherClient.getHourlyForecast(51.5074, -0.1278, "dummy-api-key", "metric")).thenReturn(mockResponse);
        WeatherResponse response = weatherService.getHourlyForecast(51.5074, -0.1278);

        assertNotNull(response);
        assertEquals(mockResponse, response.getData());
    }

    @Test
    void testGetHourlyForecast_InvalidCoordinates() {
        Exception exception = assertThrows(InvalidCoordinatesException.class, () -> weatherService.getHourlyForecast(200, 300));
        assertEquals("Invalid latitude or longitude values.", exception.getMessage());
    }

    @Test
    void testGetHourlyForecast_FeignException() {
        when(weatherClient.getHourlyForecast(51.5074, -0.1278, "dummy-api-key", "metric")).thenThrow(FeignException.class);
        assertThrows(FeignException.class, () -> weatherService.getHourlyForecast(51.5074, -0.1278));
    }

    @Test
    void testGetCityWeatherDetails_Fallback() {
        WeatherResponse response = weatherService.getCityWeatherDetailsFallback("London", new RuntimeException());
        assertEquals("Weather service unavailable. Please try again later", response.getData());
    }

    @Test
    void testGetHourlyForecast_Fallback() {
        WeatherResponse response = weatherService.getHourlyForecastFallback(51.5074, -0.1278, new RuntimeException());
        assertEquals("Hourly forecast for the given coordinates is unavailable. Please try again later", response.getData());
    }
}
