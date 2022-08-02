package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import xyz.miyayu.android.weatherapp.model.dao.AreaDao
import xyz.miyayu.android.weatherapp.repositories.AreaRepository

class AreaListFragmentViewModel(areaDao: AreaDao) : ViewModel() {
    val areaList = areaDao.getItems().asLiveData()

    fun addArea(areaName: String) {
        AreaRepository.insertArea(areaName)
    }
}