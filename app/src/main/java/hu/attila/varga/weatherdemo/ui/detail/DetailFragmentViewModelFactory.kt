package hu.attila.varga.weatherdemo.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.attila.varga.weatherdemo.data.Repository

class DetailFragmentViewModelFactory(private val repository: Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}