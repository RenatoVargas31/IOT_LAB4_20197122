package com.iot.tele_weather.api;

import com.iot.tele_weather.models.ForecastResponse;
import com.iot.tele_weather.models.LocationModel;
import com.iot.tele_weather.models.SportsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    //Consulta para las ubicaciones
    @GET("search.json")
    Call<List<LocationModel>> searchLocations(
            @Query("key") String apiKey,
            @Query("q") String query
    );
    //Consulta par los pronósticos
    @GET("forecast.json")
    Call<ForecastResponse> getForecast(
            @Query("key") String apiKey,
            @Query("q") String locationQuery,
            @Query("days") int days
    );
    //Consulta para los deportes
    @GET("sports.json")
    Call<SportsResponse> getSports(
            @Query("key") String apiKey,
            @Query("q") String locationQuery
    );

}
