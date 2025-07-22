package com.adnantech.journalApp.controller;

import com.adnantech.journalApp.api.response.WeatherResponse;
import com.adnantech.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @GetMapping("/{city}")
    public ResponseEntity<WeatherResponse> getWeatherResponse(@PathVariable String city) {
        return new ResponseEntity<>(weatherService.getWeather(city), HttpStatus.OK);
    }

}
