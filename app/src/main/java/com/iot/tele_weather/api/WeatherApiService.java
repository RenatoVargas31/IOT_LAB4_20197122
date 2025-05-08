package com.iot.tele_weather.api;

import com.iot.tele_weather.models.ForecastResponse;
import com.iot.tele_weather.models.LocationModel;
import com.iot.tele_weather.models.SportsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("search.json")
    Call<List<LocationModel>> searchLocations(
            @Query("key") String apiKey,
            @Query("q") String query
    );
    @GET("forecast.json")
    Call<ForecastResponse> getForecast(
            @Query("key") String apiKey,
            @Query("q") String locationQuery,
            @Query("days") int days
    );
    @GET("sports.json")
    Call<SportsResponse> getSports(
            @Query("key") String apiKey,
            @Query("q") String locationQuery
    );

}
