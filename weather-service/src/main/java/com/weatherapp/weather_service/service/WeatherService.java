package com.weatherapp.weather_service.service;

import com.weatherapp.weather_service.dto.WeatherResponse;

public interface WeatherService {
    WeatherResponse getCityWeatherDetails(String city);

    WeatherResponse getHourlyForecast(double lat, double lon) throws Exception;
}
