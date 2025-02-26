package com.weatherapp.weather_service.controller;

import com.weatherapp.weather_service.dto.WeatherResponse;
import com.weatherapp.weather_service.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeatherDetails(@RequestParam(required = false) String city){
        WeatherResponse weatherResponse = weatherService.getCityWeatherDetails(city);
        return new ResponseEntity<>(weatherResponse, HttpStatus.OK);
    }

    @GetMapping("/forecast")
    public ResponseEntity<WeatherResponse> getHourlyForecast(@RequestParam double lat, @RequestParam double lon) throws Exception {
        WeatherResponse weatherResponse = weatherService.getHourlyForecast(lat, lon);
        return ResponseEntity.ok(weatherResponse);
    }
}
