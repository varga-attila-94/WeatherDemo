package hu.attila.varga.weatherdemo.ui.detail

import androidx.lifecycle.ViewModel
import hu.attila.varga.weatherdemo.R
import hu.attila.varga.weatherdemo.data.Repository
import hu.attila.varga.weatherdemo.data.model.fragment.FragmentItem

class DetailViewModel(private val repository: Repository) : ViewModel() {

    private var firstFragmentData =
        arrayListOf(
            FragmentItem(
                repository.currentWeatherLiveData.value?.humidity.toString(),
                repository.app.getString(R.string.percentage),
                repository.app.getString(R.string.humidity)
            ),
            FragmentItem(
                repository.currentWeatherLiveData.value?.windSpeed.toString(),
                repository.app.getString(R.string.km_h),
                repository.app.getString(R.string.wind_speed)
            ),
            FragmentItem(
                repository.currentWeatherLiveData.value?.humidity.toString(),
                repository.app.getString(R.string.kpa),
                repository.app.getString(R.string.pressure)
            )
        )


    private var secondFragmentData =
        arrayListOf(
            FragmentItem(
                repository.currentWeatherLiveData.value?.tempMin.toString(),
                repository.app.getString(R.string.celsius),
                repository.app.getString(R.string.min_temp)
            ),
            FragmentItem(
                repository.currentWeatherLiveData.value?.temp.toString(),
                repository.app.getString(R.string.celsius),
                repository.app.getString(R.string.temp)
            ),
            FragmentItem(
                repository.currentWeatherLiveData.value?.tempMax.toString(),
                repository.app.getString(R.string.celsius),
                repository.app.getString(R.string.max_temp)
            )
        )


    fun getFragmentDataByPosition(position: Int): ArrayList<FragmentItem> {
        return if (position == 0) {
            firstFragmentData
        } else {
            secondFragmentData
        }
    }

}