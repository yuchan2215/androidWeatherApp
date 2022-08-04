package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.repositories.WeatherRepository
import xyz.miyayu.android.weatherapp.utils.Response

class WeatherViewModel(val settingDao: SettingDao, private val areaName: String) : ViewModel() {

    val status: LiveData<Response> =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Response.Loading)
            emit(WeatherRepository.fetchWeather(areaName))
        }

}