package com.iot.tele_weather.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {
    @SerializedName("forecast")
    private Forecast forecast;

    public Forecast getForecast() {
        return forecast;
    }
    // Getters y Setters
    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
    // Clase para el pronóstico
    public static class Forecast {
        @SerializedName("forecastday")
        private List<ForecastDayModel> forecastDays;

        // Getters y Setters
        public List<ForecastDayModel> getForecastDays() {
            return forecastDays;
        }

        public void setForecastDays(List<ForecastDayModel> forecastDays) {
            this.forecastDays = forecastDays;
        }
    }
}
