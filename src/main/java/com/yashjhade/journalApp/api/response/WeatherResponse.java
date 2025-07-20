package com.yashjhade.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    @JsonProperty("current")
    private Current current = new Current(); // Initialize to avoid null

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Current {
        @JsonProperty("feelslike")
        private int feelslike = 25; // Default value

        // Add empty constructor
        public Current() {}
    }

    // Add empty constructor
    public WeatherResponse() {}
}