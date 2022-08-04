package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import xyz.miyayu.android.weatherapp.repositories.AreaRepository

class TopFragmentViewModel : ViewModel() {
    val areaList = AreaRepository.getAreaList().asLiveData()
}