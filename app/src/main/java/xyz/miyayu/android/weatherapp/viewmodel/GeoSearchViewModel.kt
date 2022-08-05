package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.repositories.GeoRepository
import xyz.miyayu.android.weatherapp.utils.Response

class GeoSearchViewModel : ViewModel() {
    private val _geoResponse: MutableLiveData<Response?> = MutableLiveData(null)
    val geoResponse: LiveData<Response?> = _geoResponse

    val isLoading = geoResponse.map {
        return@map it is Response.Loading
    }

    fun fetchGeos(areaName: String) {
        _geoResponse.value = Response.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _geoResponse.value = GeoRepository.fetchGeos(areaName)
        }
    }


}