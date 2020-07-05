package hu.attila.varga.weatherdemo.ui.base

import androidx.lifecycle.MutableLiveData


class BaseActivityViewModel {

    var isProgressVisible = MutableLiveData<Boolean>()
    var isInternetAvailable = MutableLiveData<Boolean>()

    init {
        isProgressVisible.value = false
        isInternetAvailable.value = false
    }

    fun setVisibility(value: Boolean) {
        isProgressVisible.postValue(value)
    }

    fun setInternetAvailable(value: Boolean) {
        isInternetAvailable.postValue(value)
    }

}