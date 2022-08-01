package xyz.miyayu.android.weatherapp.repositories

import retrofit2.Response
import xyz.miyayu.android.weatherapp.network.WeatherApi
import xyz.miyayu.android.weatherapp.network.json.Weather

object WeatherRepository {
    suspend fun getWeather(areaName: String): Response<Weather> {
        val apiKey = SettingRepository.getApiKey() ?: ""
        return WeatherApi.retrofitService.getWeather(apiKey, areaName)
    }
}