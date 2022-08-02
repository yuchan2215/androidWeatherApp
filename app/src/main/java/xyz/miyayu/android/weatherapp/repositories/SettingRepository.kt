package xyz.miyayu.android.weatherapp.repositories

import xyz.miyayu.android.weatherapp.WeatherApplication

object SettingRepository {
    private val application by lazy {
        WeatherApplication.instance
    }

    suspend fun getApiKey(): String? {
        return application.database.settingDao().getItemOnce().value
    }
}