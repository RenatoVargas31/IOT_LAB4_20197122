package com.iot.tele_weather.models;

import com.google.gson.annotations.SerializedName;

public class SportEvent {
    @SerializedName("stadium")
    private String stadium;

    @SerializedName("country")
    private String country;

    @SerializedName("region")
    private String region;

    @SerializedName("tournament")
    private String tournament;

    @SerializedName("start")
    private String start;

    @SerializedName("match")
    private String match;

    // Getters
    public String getStadium() {
        return stadium;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getTournament() {
        return tournament;
    }

    public String getStart() {
        return start;
    }

    public String getMatch() {
        return match;
    }

    // Método para obtener la ubicación completa
    public String getFullLocation() {
        StringBuilder location = new StringBuilder();

        if (stadium != null && !stadium.isEmpty()) {
            location.append(stadium);
        }

        if (country != null && !country.isEmpty()) {
            if (location.length() > 0) {
                location.append(", ");
            }
            location.append(country);
        }

        if (region != null && !region.isEmpty()) {
            if (location.length() > 0) {
                location.append(", ");
            }
            location.append(region);
        }

        return location.length() > 0 ? location.toString() : "Ubicación no disponible";
    }
}
