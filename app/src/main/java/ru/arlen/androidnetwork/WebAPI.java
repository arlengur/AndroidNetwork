package ru.arlen.androidnetwork;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.arlen.androidnetwork.model.Weather;

import java.util.List;

public interface WebAPI {
    @GET("/data/2.5/forecast/daily?")
    Call<Weather> getWeather(
            @Query("q") String city,
            @Query("units") String metric,
            @Query("cnt") String days,
            @Query("appid") String key);
}
