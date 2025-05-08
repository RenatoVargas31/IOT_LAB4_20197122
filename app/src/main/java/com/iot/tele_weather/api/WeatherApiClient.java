package com.iot.tele_weather.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiClient {
    private static final String BASE_URL = "https://api.weatherapi.com/v1/";
    private static final String API_KEY = "14818a8ad9974362a04160649250705";

    private static WeatherApiService instance;

    public static WeatherApiService getInstance() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(WeatherApiService.class);
        }
        return instance;
    }

    public static String getApiKey() {
        return API_KEY;
    }
}
