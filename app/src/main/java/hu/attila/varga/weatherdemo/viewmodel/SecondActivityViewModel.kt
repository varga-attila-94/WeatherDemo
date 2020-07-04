package hu.attila.varga.weatherdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SecondActivityViewModel : ViewModel() {

    var contentText = MutableLiveData<String>()

    init {
        contentText.value = "Empty Page"
    }

    fun setContentText(input: String) {
        contentText.value = input
    }

}
