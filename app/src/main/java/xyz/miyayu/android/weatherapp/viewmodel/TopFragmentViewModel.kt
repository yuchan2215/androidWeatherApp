package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import xyz.miyayu.android.weatherapp.model.dao.AreaDao

class TopFragmentViewModel(areaDao: AreaDao) : ViewModel() {
    val areaList = areaDao.getItems().asLiveData()
}