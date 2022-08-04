package xyz.miyayu.android.weatherapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.viewmodel.WeatherViewModel

class WeatherViewModelFactory(
    private val area: Area
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(area) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}