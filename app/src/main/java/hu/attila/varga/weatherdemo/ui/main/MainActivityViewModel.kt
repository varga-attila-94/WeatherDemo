package hu.attila.varga.weatherdemo.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hu.attila.varga.weatherdemo.data.Repository
import hu.attila.varga.weatherdemo.data.model.current.Coord
import hu.attila.varga.weatherdemo.data.model.current.CurrentData
import hu.attila.varga.weatherdemo.data.model.forecast.ForecastItemData
import hu.attila.varga.weatherdemo.data.model.progress.ProgressData

class MainActivityViewModel(val app: Application) : AndroidViewModel(app) {

    var showProgressBar: MutableLiveData<ProgressData>
    var currentWeatherLiveData = MutableLiveData<CurrentData>()
    var forecastListLiveData = MutableLiveData<List<ForecastItemData>>()
    var repository: Repository = Repository(app)

    init {
        repository = Repository(app)
        showProgressBar = repository.progress
        currentWeatherLiveData = repository.currentWeatherLiveData
        forecastListLiveData = repository.forecastListLiveData
    }

    fun getAllData(coord: Coord?) {
        repository.getAllData(coord)
    }

}
