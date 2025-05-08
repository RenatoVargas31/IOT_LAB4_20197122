package com.iot.tele_weather.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SportsResponse {
    @SerializedName("football")
    private List<SportEvent> footballEvents;

    @SerializedName("cricket")
    private List<SportEvent> cricketEvents;

    @SerializedName("golf")
    private List<SportEvent> golfEvents;

    // Getters
    public List<SportEvent> getFootballEvents() {
        return footballEvents;
    }

    public List<SportEvent> getCricketEvents() {
        return cricketEvents;
    }

    public List<SportEvent> getGolfEvents() {
        return golfEvents;
    }

    // Método para obtener todos los eventos
    public List<SportEvent> getAllEvents() {
        List<SportEvent> allEvents = new ArrayList<>();

        if (footballEvents != null) {
            allEvents.addAll(footballEvents);
        }

        if (cricketEvents != null) {
            allEvents.addAll(cricketEvents);
        }

        if (golfEvents != null) {
            allEvents.addAll(golfEvents);
        }

        return allEvents;
    }
}
