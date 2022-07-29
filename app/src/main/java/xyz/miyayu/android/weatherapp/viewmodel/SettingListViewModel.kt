package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.*
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.model.entity.Setting

class SettingListViewModel(
    settingDao: SettingDao,
    apiKeyTapEvent: () -> Unit,
    areasTapEvent: () -> Unit
) : ViewModel() {

    private val _listItems =
        MutableLiveData<List<ListData>>(
            arrayListOf(
                ListData("APIキー", tapEvent = apiKeyTapEvent),
                ListData("地域の設定", tapEvent = areasTapEvent)
            )
        )
    val listItems: LiveData<List<ListData>>
        get() = _listItems

    val apiKey: LiveData<Setting> = settingDao.getItem().asLiveData()

    fun replaceItems(newList: List<ListData>) {
        _listItems.value = newList
    }
}

data class ListData(val title: String, var value: String = "", val tapEvent: () -> Unit = {})

class SettingListViewModelFactory(
    private val settingDao: SettingDao,
    private val apiKeyTapEvent: () -> Unit,
    private val areasTapEvent: () -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingListViewModel(settingDao, apiKeyTapEvent, areasTapEvent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}