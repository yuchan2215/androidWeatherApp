package xyz.miyayu.android.weatherapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.miyayu.android.weatherapp.model.dao.AreaDao
import xyz.miyayu.android.weatherapp.viewmodel.AreaListFragmentViewModel

class AreaListFragmentViewModelFactory(
    private val areaDao: AreaDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AreaListFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AreaListFragmentViewModel(areaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}