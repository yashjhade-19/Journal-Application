package com.yashjhade.journalApp.controller;

import com.yashjhade.journalApp.api.response.WeatherResponse;
import com.yashjhade.journalApp.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

//    @GetMapping("/greet")
//    @Operation(summary = "Greet user with weather information")
//    public ResponseEntity<?> greeting() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
//        String greeting = "";
//
//        if (weatherResponse != null && weatherResponse.getCurrent() != null) {
//            greeting = ", Weather feels like " + weatherResponse.getCurrent().getFeelslike() + "°C";
//        }
//
//        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
//    }

    @GetMapping("/{city}")
    @Operation(summary = "Get weather for specific city")
    public ResponseEntity<?> getWeatherByCity(@PathVariable String city) {
        try {
            WeatherResponse response = weatherService.getWeather(city);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Weather service temporarily unavailable");
        }
    }
}