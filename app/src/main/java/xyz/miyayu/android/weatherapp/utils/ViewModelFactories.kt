package xyz.miyayu.android.weatherapp.utils

import xyz.miyayu.android.weatherapp.viewmodel.factory.AreaListFragmentViewModelFactory
import xyz.miyayu.android.weatherapp.viewmodel.factory.SettingViewModelFactory
import xyz.miyayu.android.weatherapp.viewmodel.factory.TopFragmentViewModelFactory
import xyz.miyayu.android.weatherapp.viewmodel.factory.WeatherViewModelFactory

/**
 * ViewModelFactoryを用意しておくObject
 */
object ViewModelFactories {
    fun getSettingViewModelFactory(): SettingViewModelFactory {
        return SettingViewModelFactory()
    }

    fun getWeatherViewModelFactory(areaName: String): WeatherViewModelFactory {
        return WeatherViewModelFactory(areaName)
    }

    fun getTopFragmentViewModelFactory(): TopFragmentViewModelFactory {
        return TopFragmentViewModelFactory()
    }

    fun getAreaListFragmentViewModelFactory(): AreaListFragmentViewModelFactory {
        return AreaListFragmentViewModelFactory()
    }
}