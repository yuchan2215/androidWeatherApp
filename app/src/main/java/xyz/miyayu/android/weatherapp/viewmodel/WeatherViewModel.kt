package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.repositories.WeatherRepository
import xyz.miyayu.android.weatherapp.utils.Response

class WeatherViewModel(val settingDao: SettingDao) : ViewModel() {

    private val _status = MutableLiveData<Response>()
    val status: LiveData<Response> = _status


    fun fetchWeather(areaName: String) {

        _status.value = Response.Loading
        //サーバー上から天気を取得してくる
        viewModelScope.launch {
            _status.value = WeatherRepository.fetchWeather(areaName)
        }
    }
}