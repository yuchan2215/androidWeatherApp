package xyz.miyayu.android.weatherapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.viewmodel.WeatherViewModel

class WeatherViewModelFactory(
    private val settingDao: SettingDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(settingDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}