package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.network.json.weather.Weather
import xyz.miyayu.android.weatherapp.repositories.AreaRepository
import xyz.miyayu.android.weatherapp.repositories.WeatherRepository
import xyz.miyayu.android.weatherapp.utils.Response


class WeatherViewModel(
    private val area: Area
) : ViewModel() {

    val status: LiveData<Response<Weather>> =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Response.Loading())
            emit(WeatherRepository.fetchWeather(area.name))
        }

    fun deleteArea() {
        viewModelScope.launch {
            AreaRepository.deleteArea(area)
        }
    }
}