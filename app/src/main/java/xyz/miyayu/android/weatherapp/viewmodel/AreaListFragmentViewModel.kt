package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.repositories.AreaRepository

class AreaListFragmentViewModel() : ViewModel() {
    val areaList = AreaRepository.getAreaList().asLiveData()


    fun deleteArea(area: Area) {
        viewModelScope.launch {
            AreaRepository.deleteArea(area)
        }
    }
}