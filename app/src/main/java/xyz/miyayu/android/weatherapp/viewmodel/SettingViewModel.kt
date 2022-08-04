package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.model.entity.Setting
import xyz.miyayu.android.weatherapp.repositories.AreaRepository
import xyz.miyayu.android.weatherapp.repositories.SettingRepository

class SettingViewModel : ViewModel() {
    //地域一覧
    val areaList: LiveData<List<Area>> = AreaRepository.getAreaList().asLiveData()

    //APIキー
    val apiKey: LiveData<Setting> = SettingRepository.getApiKeyFlow().asLiveData()

}