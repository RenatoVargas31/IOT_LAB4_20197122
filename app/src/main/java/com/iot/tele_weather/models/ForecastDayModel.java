package com.iot.tele_weather.models;

import com.google.gson.annotations.SerializedName;

public class ForecastDayModel {
    @SerializedName("date")
    private String date;

    @SerializedName("day")
    private DayForecastModel day;

    // Getters y Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DayForecastModel getDay() {
        return day;
    }

    public void setDay(DayForecastModel day) {
        this.day = day;
    }

    // Clase interna para los datos del día
    public static class DayForecastModel {
        @SerializedName("maxtemp_c")
        private double maxTempC;

        @SerializedName("mintemp_c")
        private double minTempC;

        @SerializedName("avgtemp_c")
        private double avgTempC;

        @SerializedName("condition")
        private WeatherCondition condition;

        // Getters y Setters
        public double getMaxTempC() {
            return maxTempC;
        }

        public void setMaxTempC(double maxTempC) {
            this.maxTempC = maxTempC;
        }

        public double getMinTempC() {
            return minTempC;
        }

        public void setMinTempC(double minTempC) {
            this.minTempC = minTempC;
        }

        public double getAvgTempC() {
            return avgTempC;
        }

        public void setAvgTempC(double avgTempC) {
            this.avgTempC = avgTempC;
        }

        public WeatherCondition getCondition() {
            return condition;
        }

        public void setCondition(WeatherCondition condition) {
            this.condition = condition;
        }
    }

    // Clase para la condición del clima
    public static class WeatherCondition {
        @SerializedName("text")
        private String text;

        @SerializedName("icon")
        private String icon;

        @SerializedName("code")
        private int code;

        // Getters y Setters
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
