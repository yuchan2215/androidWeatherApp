package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.model.dao.AreaDao
import xyz.miyayu.android.weatherapp.repositories.AreaRepository

class AreaListFragmentViewModel(areaDao: AreaDao) : ViewModel() {
    val areaList = areaDao.getItems().asLiveData()

    fun addArea(areaName: String) {
        viewModelScope.launch {
            AreaRepository.insertArea(areaName)
        }
    }
}