package xyz.miyayu.android.weatherapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import xyz.miyayu.android.weatherapp.network.json.Weather
import java.util.*

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("appid") apiKey: String,
        @Query("q") query: String,
        @Query("lang") lang: String = getLang()
    ): Response<Weather>

    companion object {
        fun getLang(): String {
            val isJpn = Locale.getDefault() == Locale.JAPAN
            return if (isJpn) "ja" else "en"
        }
    }
}

