package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import xyz.miyayu.android.weatherapp.model.dao.AreaDao
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.model.entity.Setting

class SettingViewModel(areaDao: AreaDao, settingDao: SettingDao) : ViewModel() {
    //地域一覧
    val areaList: LiveData<List<Area>> = areaDao.getItems().asLiveData()

    //APIキー
    val apiKey: LiveData<Setting> =
        settingDao.getItem().asLiveData()

}

class SettingViewModelFactory(
    private val settingDao: SettingDao,
    private val areaDao: AreaDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(areaDao, settingDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}