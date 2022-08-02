package xyz.miyayu.android.weatherapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.miyayu.android.weatherapp.model.dao.AreaDao
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.viewmodel.SettingViewModel

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