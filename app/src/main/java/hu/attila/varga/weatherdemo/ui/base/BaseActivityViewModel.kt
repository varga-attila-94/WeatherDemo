package hu.attila.varga.weatherdemo.ui.base

import androidx.lifecycle.MutableLiveData

class BaseActivityViewModel {

    var isProgressVisible = MutableLiveData<Boolean>()

    init {
        isProgressVisible.value = true
    }

    fun setVisibility(value: Boolean) {
        isProgressVisible.value = value
    }

}