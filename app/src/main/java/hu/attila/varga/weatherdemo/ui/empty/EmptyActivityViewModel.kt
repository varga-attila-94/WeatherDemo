package hu.attila.varga.weatherdemo.ui.empty

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmptyActivityViewModel : ViewModel() {

    var contentText = MutableLiveData<String>()

    init {
        contentText.value = "Empty Page"
    }

    fun setContentText(input: String) {
        contentText.value = input
    }

}
