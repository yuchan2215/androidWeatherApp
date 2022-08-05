package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.network.json.direct.Direct
import xyz.miyayu.android.weatherapp.repositories.GeoRepository
import xyz.miyayu.android.weatherapp.utils.Response

class GeoSearchViewModel : ViewModel() {
    private val _geoResponse: MutableLiveData<Response<List<Direct>>?> = MutableLiveData(null)
    val geoResponse: LiveData<Response<List<Direct>>?> = _geoResponse

    val isLoading = geoResponse.map {
        return@map it is Response.Loading
    }

    fun fetchGeos(areaName: String) {
        _geoResponse.value = Response.Loading()
        viewModelScope.launch(Dispatchers.Main) {
            _geoResponse.value = GeoRepository.fetchGeos(areaName)
        }
    }


}