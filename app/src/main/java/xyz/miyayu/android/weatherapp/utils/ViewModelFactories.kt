package xyz.miyayu.android.weatherapp.utils

import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.viewmodel.SettingViewModelFactory

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
}