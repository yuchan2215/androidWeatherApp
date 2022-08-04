package xyz.miyayu.android.weatherapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.miyayu.android.weatherapp.viewmodel.TopFragmentViewModel

class TopFragmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TopFragmentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}