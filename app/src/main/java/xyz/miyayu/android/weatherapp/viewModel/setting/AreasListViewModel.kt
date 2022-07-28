package xyz.miyayu.android.weatherapp.viewModel.setting

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import xyz.miyayu.android.weatherapp.data.Area
import xyz.miyayu.android.weatherapp.data.AreaDao

class AreasListViewModel(private val areaDao: AreaDao): ViewModel() {
    val allAreas: LiveData<List<Area>> = areaDao.getItems().asLiveData()

    fun insertArea(areaName: String){
        val area = Area(
            name = areaName
        )
        insertArea(area)
    }
    private fun insertArea(area: Area){
        viewModelScope.launch {
            areaDao.insert(area)
        }
    }

    fun deleteArea(area: Area){
        viewModelScope.launch {
            areaDao.delete(area)
        }
    }
}
class AreasListViewModelFactory(private val areaDao: AreaDao):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AreasListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AreasListViewModel(areaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}