package xyz.miyayu.android.weatherapp.repositories

import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.model.entity.Setting

object SettingRepository {
    private val application by lazy {
        WeatherApplication.instance
    }

    suspend fun getApiKey(): String? {
        return application.database.settingDao().getItem().value
    }

    fun getApiKeyFlow() = application.database.settingDao().getItemFlow()

    suspend fun setApiKey(apiKey: String) {
        val setting = Setting(value = apiKey)
        application.database.settingDao().insert(setting)
    }
}