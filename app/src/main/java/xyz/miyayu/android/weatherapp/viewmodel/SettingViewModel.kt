package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.*
import xyz.miyayu.android.weatherapp.model.dao.AreaDao
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.model.entity.Area

class SettingViewModel(areaDao: AreaDao, settingDao: SettingDao) {
    //地域一覧
    val areaList: LiveData<List<Area>> = areaDao.getItems().asLiveData()

    //地域数
    val areaCount: LiveData<Int> = areaList.map { list -> list.size }

    //APIキー
    val apiKey: LiveData<String> =
        settingDao.getItem().asLiveData().map { setting -> setting.value ?: "" }
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