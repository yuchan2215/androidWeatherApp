package xyz.miyayu.android.weatherapp.utils

import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.viewmodel.factory.SettingViewModelFactory
import xyz.miyayu.android.weatherapp.viewmodel.factory.WeatherViewModelFactory

/**
 * ViewModelFactoryを用意しておくObject
 */
object ViewModelFactories {
    fun getSettingViewModelFactory(): SettingViewModelFactory {
        val application = WeatherApplication.instance
        val areaDao = application.database.areaDao()
        val settingDao = application.database.settingDao()
        return SettingViewModelFactory(settingDao, areaDao)
    }

    fun getWeatherViewModelFactory(): WeatherViewModelFactory {
        val application = WeatherApplication.instance
        val settingDao = application.database.settingDao()
        return WeatherViewModelFactory(settingDao)
    }
}