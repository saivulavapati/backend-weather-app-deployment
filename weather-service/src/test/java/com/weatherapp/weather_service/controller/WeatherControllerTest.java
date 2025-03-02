package com.weatherapp.weather_service.controller;

import com.weatherapp.weather_service.dto.WeatherResponse;
import com.weatherapp.weather_service.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WeatherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    void testGetWeatherDetails_Success() throws Exception {
        String city = "London";
        WeatherResponse response = new WeatherResponse("{ 'temp': 20 }");
        when(weatherService.getCityWeatherDetails(city)).thenReturn(response);

        mockMvc.perform(get("/api/v1/weather")
                        .param("city", city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("{ 'temp': 20 }"));
    }

    @Test
    void testGetHourlyForecast_Success() throws Exception {
        double lat = 40.7128;
        double lon = -74.0060;
        WeatherResponse response = new WeatherResponse("{ 'hourly': [] }");
        when(weatherService.getHourlyForecast(lat, lon)).thenReturn(response);

        mockMvc.perform(get("/api/v1/weather/forecast")
                        .param("lat", String.valueOf(lat))
                        .param("lon", String.valueOf(lon))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("{ 'hourly': [] }"));
    }
}
