package com.yashjhade.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    @JsonProperty("current")
    private Current current;

    /* ---------- nested DTOs ---------- */

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Current {
        @JsonProperty("observation_time") private String observationTime;
        private int temperature;
        @JsonProperty("weather_code") private int weatherCode;
        @JsonProperty("weather_icons") private List<String> weatherIcons;
        @JsonProperty("weather_descriptions") private List<String> weatherDescriptions;
        private Astro astro;
        @JsonProperty("air_quality") private AirQuality airQuality;
        private int feelslike;
        // add other fields if you need them
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Astro {
        private String sunrise;
        private String sunset;
        private String moonrise;
        private String moonset;
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AirQuality {
        private String co;
        private String no2;
        private String o3;
        private String so2;
        @JsonProperty("pm10") private String pm10;
    }
}
