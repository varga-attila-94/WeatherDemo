package hu.attila.varga.weatherdemo.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.attila.varga.weatherdemo.data.Repository
import hu.attila.varga.weatherdemo.data.local.SharedPreferenceLiveData
import hu.attila.varga.weatherdemo.data.model.current.CurrentData
import hu.attila.varga.weatherdemo.data.model.forecast.ForecastItemData

class MainActivityViewModel(val app: Application) : AndroidViewModel(app) {

    var showProgressBar: LiveData<Boolean>
    var currentWeatherLiveData = MutableLiveData<CurrentData>()
    var forecastListLiveData = MutableLiveData<List<ForecastItemData>>()
    var currentLocationLiveData: SharedPreferenceLiveData<String>
    var repository: Repository = Repository(app)

    init {
        repository = Repository(app)
        getCurrentWeather()
        getForecast()
        currentLocationLiveData = repository.getCurrentLocation()
        showProgressBar = repository.progress
    }


    fun getCurrentWeather() {
        currentWeatherLiveData = repository.getCurrentWeather()
    }

    fun getForecast() {
        forecastListLiveData = repository.getForecast()
    }


}
