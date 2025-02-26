package com.weatherapp.weather_service.service;

import com.weatherapp.weather_service.client.WeatherClient;
import com.weatherapp.weather_service.dto.WeatherResponse;
import com.weatherapp.weather_service.exception.BadRequestException;
import com.weatherapp.weather_service.exception.InvalidCoordinatesException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService{

    @Value("${openweather.api.key}")
    private String apiKey;

    @Autowired
    private WeatherClient weatherClient;

    @CircuitBreaker(name = "weather-service",fallbackMethod = "getCityWeatherDetailsFallback")
    @Override
    public WeatherResponse getCityWeatherDetails(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new BadRequestException("City cannot be empty.");
        }
        try {
            String weatherData = weatherClient.getWeather(city, apiKey, "metric");
            WeatherResponse weatherResponse = new WeatherResponse();
            weatherResponse.setData(weatherData);
            return weatherResponse;
        } catch (FeignException exception){
            throw exception;
        }
    }

    @CircuitBreaker(name = "weather-service",fallbackMethod = "getHourlyForecastFallback")
    @Override
    public WeatherResponse getHourlyForecast(double lat, double lon) {
        try {
            if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
                throw new InvalidCoordinatesException("Invalid latitude or longitude values.");
            }
            String weatherData = weatherClient.getHourlyForecast(lat, lon, apiKey, "metric");
            WeatherResponse weatherResponse = new WeatherResponse();
            weatherResponse.setData(weatherData);
            return weatherResponse;
        } catch (FeignException e) {
            throw e;
        }
    }

    public WeatherResponse getCityWeatherDetailsFallback(String city, Throwable throwable){
        return new WeatherResponse("Weather service unavailable. Please try again later");
    }

    public WeatherResponse getHourlyForecastFallback(double lat, double lon,Throwable throwable) {
        return new WeatherResponse("Hourly forecast for the given coordinates is unavailable. Please try again later");
    }

}
