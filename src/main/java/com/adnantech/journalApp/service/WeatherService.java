package com.adnantech.journalApp.service;

import com.adnantech.journalApp.api.response.WeatherResponse;
import com.adnantech.journalApp.cache.AppCache;
import com.adnantech.journalApp.utils.Placeholders;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String API_KEY;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @PostConstruct
    public void logRedisInfo() {
        var connection = redisConnectionFactory.getConnection();
        log.info("🔗 Connected to Redis at: {}", connection.getNativeConnection());
    }

    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null) {
            log.info("Response from Redis {}", weatherResponse);
            return weatherResponse;
        } else {
            log.info("Response from Real Api ");
            String finalAPI = appCache.APP_CACHE.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.API_KEY, API_KEY).replace(Placeholders.CITY, city);
            log.info("API {}", finalAPI);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if (body != null){
                redisService.set("weather_of_" + city, body, 300l);
            }
            return body;
        }
    }


//    public WeatherResponse setWeather(String city) {
//        String finalAPI = Constants.API.replace("API_KEY", API_KEY).replace("CITY", city);
//        log.info("API {}", finalAPI);
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("key", "value");
//        String requestBody = "{}";
//        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
//
//        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.POST, httpEntity, WeatherResponse.class);
//        WeatherResponse body = response.getBody();
//        return body;
//    }

}
