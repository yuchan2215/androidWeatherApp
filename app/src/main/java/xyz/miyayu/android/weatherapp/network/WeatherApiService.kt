package xyz.miyayu.android.weatherapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("appid") apiKey: String,
        @Query("q") query: String
    ): Response<Weather>
}