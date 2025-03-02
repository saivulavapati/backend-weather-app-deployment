package com.weatherapp.weather_service.exception;

public class WeatherClientException extends RuntimeException {
    public WeatherClientException(String message) {
        super(message);
    }
}
