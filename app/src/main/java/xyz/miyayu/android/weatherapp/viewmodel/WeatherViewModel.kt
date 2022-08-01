package xyz.miyayu.android.weatherapp.viewmodel

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.network.WeatherApi
import xyz.miyayu.android.weatherapp.network.json.Weather

class WeatherViewModel(val settingDao: SettingDao) : ViewModel() {
    val setting = settingDao.getItem().asLiveData()

    private val _status = MutableLiveData<WeatherApiStatus>(WeatherApiStatus.NONE)
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

    companion object {
        enum class WeatherApiStatus(
            val loadingVisibility: Int = View.GONE,
            val resultVisibility: Int = View.GONE,
            val errorVisibility: Int = View.GONE,
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
                errorVisibility = View.VISIBLE,
                errorMessage = WeatherApplication.instance.getString(R.string.error_not_found)
            ),
            NETWORK_ERROR(
                errorVisibility = View.VISIBLE,
                errorMessage = WeatherApplication.instance.getString(R.string.error)
            )
        }
    }
}