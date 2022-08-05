package xyz.miyayu.android.weatherapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.miyayu.android.weatherapp.viewmodel.GeoSearchViewModel
import xyz.miyayu.android.weatherapp.viewmodel.WeatherViewModel

class GeoSearchViewModelFactory(
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GeoSearchViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}