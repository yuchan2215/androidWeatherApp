package xyz.miyayu.android.weatherapp.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.network.json.Weather
import xyz.miyayu.android.weatherapp.repositories.WeatherRepository

class WeatherViewModel(val settingDao: SettingDao) : ViewModel() {

    private val _status = MutableLiveData<WeatherApiStatus>(WeatherApiStatus.NONE)
    val status: LiveData<WeatherApiStatus> = _status

    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather> = _weather

    private fun setApiStatus(status: WeatherApiStatus) {
        _status.value = status
    }

    private fun setWeather(weather: Weather) {
        _weather.value = weather
    }

    fun updateWeather(areaName: String) {
        setApiStatus(WeatherApiStatus.LOADING)
        viewModelScope.launch {
            try {
                val requestResponse = WeatherRepository.getWeather(areaName)
                if (requestResponse.isSuccessful && requestResponse.body() != null) {
                    setWeather(requestResponse.body()!!)
                    setApiStatus(WeatherApiStatus.DONE)
                } else {
                    val status = when (requestResponse.raw().code) {
                        401 -> WeatherApiStatus.UNAUTHORIZED
                        404 -> WeatherApiStatus.NOT_FOUND
                        else -> WeatherApiStatus.ERROR
                    }
                    setApiStatus(status)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                setApiStatus(WeatherApiStatus.NETWORK_ERROR)
            }
        }
    }


    companion object {
        enum class WeatherApiStatus(
            val loadingVisibility: Int = View.GONE,
            val resultVisibility: Int = View.GONE,
            val errorVisibility: Int = View.GONE,
            val errorNotFoundVisibility: Int = View.GONE,
            val errorMessage: String = ""
        ) {
            NONE,
            LOADING(loadingVisibility = View.VISIBLE),
            DONE(resultVisibility = View.VISIBLE),
            ERROR(
                errorVisibility = View.VISIBLE,
                errorMessage = WeatherApplication.instance.getString(R.string.error)
            ),
            APIKEY_NOT_SET(
                errorVisibility = View.VISIBLE,
                errorMessage = WeatherApplication.instance.getString(R.string.error_api_key_not_found)
            ),
            UNAUTHORIZED(
                errorVisibility = View.VISIBLE,
                errorMessage = WeatherApplication.instance.getString(R.string.error_unauthorized)
            ),
            NOT_FOUND(
                errorNotFoundVisibility = View.VISIBLE,
                errorMessage = WeatherApplication.instance.getString(R.string.error_not_found)
            ),
            NETWORK_ERROR(
                errorVisibility = View.VISIBLE,
                errorMessage = WeatherApplication.instance.getString(R.string.error)
            )
        }
    }
}