package xyz.miyayu.android.weatherapp.viewModel.setting

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import xyz.miyayu.android.weatherapp.model.entity.Setting
import xyz.miyayu.android.weatherapp.model.dao.SettingDao

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

    private val apiKey: LiveData<Setting> = settingDao.getItem().asLiveData()

    //observeをすることでAPIキー等の変更が反映される。
    fun observe(owner: LifecycleOwner) {
        val apiKeyObserver = Observer<Setting> { setting ->
            _listItems.value = _listItems.value.apply {
                this!![0].value = setting?.value ?: ""
            }
        }
        apiKey.observe(owner, apiKeyObserver)
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
            return SettingListViewModel(settingDao, apiKeyTapEvent,areasTapEvent) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}