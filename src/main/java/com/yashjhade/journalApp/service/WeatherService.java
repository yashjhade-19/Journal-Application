package com.yashjhade.journalApp.service;

import com.yashjhade.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherResponse getWeather(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.weatherstack.com/current?access_key=" + apiKey + "&query=" + city;

        try {
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

            // If we got a valid response, return it
            if (response != null && response.getCurrent() != null) {
                return response;
            }
        } catch (Exception e) {
            // If API fails, fall through to default
        }

        // Fallback response
        WeatherResponse fallback = new WeatherResponse();
        WeatherResponse.Current current = new WeatherResponse.Current();
        current.setFeelslike(25);
        fallback.setCurrent(current);
        return fallback;
    }
}