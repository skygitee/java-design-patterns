package com.iluwatar.soa.services;

import com.iluwatar.soa.model.WeatherCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalizedGreetingService {

  @Autowired
  private GreetingService greetingService;

  @Autowired
  private WeatherService weatherService;

  public String generateGreeting() {
    String weatherGreeting = getWeatherGreeting();
    return weatherGreeting + "! " + greetingService.getGenericGreeting() + ".";
  }

  private String getWeatherGreeting() {
    WeatherCondition currentWeather = weatherService.getCurrentWeather();
    switch (currentWeather) {
      case SUNNY:
        return "What a good sunny day!";
      case RAINY:
        return "What a rainy day!";
      case CLOUDY:
        return "What a cloudy day!";
      case FOGGY:
        return "What a foggy day!";
      default:
        // error case
        return "unexpected weather condition";
    }
  }


}
