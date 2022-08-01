package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.network.Weather
import xyz.miyayu.android.weatherapp.network.WeatherApi

class WeatherViewModel(val settingDao: SettingDao) : ViewModel() {
    val setting = settingDao.getItem().asLiveData()

    private val _status = MutableLiveData<WeatherApiStatus>()
    val status: LiveData<WeatherApiStatus> = _status

    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather> = _weather

    /**
     * 天気をネットワーク上から取得する。
     */
    fun getWeather(area: String, key: String?) {
        _status.value = WeatherApiStatus.LOADING
        if (key == null) {
            _status.value = WeatherApiStatus.APIKEY_NOT_SET
            return
        }
        viewModelScope.launch {
            try {
                val request = WeatherApi.retrofitService.getWeather(key, area)
                if (request.isSuccessful) {
                    _weather.value = request.body()
                    _status.value = WeatherApiStatus.DONE
                } else {
                    _status.value = when (request.raw().code) {
                        401 -> WeatherApiStatus.UNAUTHORIZED
                        404 -> WeatherApiStatus.NOT_FOUND
                        else -> WeatherApiStatus.ERROR
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = WeatherApiStatus.NETWORK_ERROR
                _weather.value = null
            }
        }

    }

    enum class WeatherApiStatus {
        LOADING, ERROR, DONE, APIKEY_NOT_SET, UNAUTHORIZED, NOT_FOUND, NETWORK_ERROR
    }
}