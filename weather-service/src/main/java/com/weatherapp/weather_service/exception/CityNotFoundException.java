package com.weatherapp.weather_service.exception;

public class CityNotFoundException extends RuntimeException{
    private String message;

    public CityNotFoundException(String message) {
        super(message);
    }
}
