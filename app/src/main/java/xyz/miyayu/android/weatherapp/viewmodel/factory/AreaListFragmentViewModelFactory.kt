package xyz.miyayu.android.weatherapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.miyayu.android.weatherapp.viewmodel.AreaListFragmentViewModel

class AreaListFragmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AreaListFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AreaListFragmentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}