package xyz.miyayu.android.weatherapp.viewModel.setting

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import java.util.*

class SettingListViewModel : ViewModel() {

    private val _listItems =
        MutableLiveData<List<ListData>>(arrayListOf(ListData("APIキー"), ListData("地域の設定")))
    val listItems: LiveData<List<ListData>>
        get() = _listItems

    private val _apiKey = MutableLiveData<String>("")
    val apiKey: LiveData<String>
        get() = _apiKey

    //デバッグ用
    fun updateItem() {
        _apiKey.value = UUID.randomUUID().toString()
    }

    //observeをすることでAPIキー等の変更が反映される。
    fun observe(owner: LifecycleOwner) {
        val apiKeyObserver = Observer<String> {
            _listItems.value = _listItems.value.apply {
                this!![0].value = it
            }
        }
        _apiKey.observe(owner, apiKeyObserver)
    }


}

data class ListData(val title: String, var value: String = "")