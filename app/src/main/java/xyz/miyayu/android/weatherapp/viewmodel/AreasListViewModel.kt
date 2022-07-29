package xyz.miyayu.android.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import xyz.miyayu.android.weatherapp.model.dao.AreaDao
import xyz.miyayu.android.weatherapp.model.entity.Area

class AreasListViewModel(areaDao: AreaDao) : ViewModel() {
    val allAreas: LiveData<List<Area>> = areaDao.getItems().asLiveData()
}

class AreasListViewModelFactory(private val areaDao: AreaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AreasListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AreasListViewModel(areaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}