package hu.attila.varga.weatherdemo.ui.base

import androidx.lifecycle.MutableLiveData


class BaseActivityViewModel {

    var isProgressVisible = MutableLiveData<Boolean>()
    var progressMessage = MutableLiveData<String>()
    var isInternetAvailable = MutableLiveData<Boolean>()

    init {
        isProgressVisible.value = false
        isInternetAvailable.value = false
    }

    fun setVisibility(value: Boolean) {
        isProgressVisible.postValue(value)
    }

    fun setMessage(value: String) {
        progressMessage.postValue(value)
    }

    fun setInternetAvailable(value: Boolean) {
        isInternetAvailable.postValue(value)
    }

}