package hu.attila.varga.weatherdemo.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hu.attila.varga.weatherdemo.model.current.Coord
import hu.attila.varga.weatherdemo.model.current.CurrentData
import hu.attila.varga.weatherdemo.model.current.CurrentWeatherResponse
import hu.attila.varga.weatherdemo.model.forecast.ForecastItemData
import hu.attila.varga.weatherdemo.model.forecast.ForecastResponse
import hu.attila.varga.weatherdemo.utils.PreferenceHelper
import hu.attila.varga.weatherdemo.utils.Utils.Companion.DEFAULT_BUDAPEST_LAT
import hu.attila.varga.weatherdemo.utils.Utils.Companion.DEFAULT_BUDAPEST_LON
import hu.attila.varga.weatherdemo.utils.Utils.Companion.DEGREE_SYMBOL
import hu.attila.varga.weatherdemo.utils.Utils.Companion.EXCLUDE_VALUES
import hu.attila.varga.weatherdemo.utils.Utils.Companion.KELVIN
import hu.attila.varga.weatherdemo.utils.Utils.Companion.OPEN_WEATHER_MAP_API_KEY
import hu.attila.varga.weatherdemo.utils.network.RetrofitInstance.Companion.getRetrofitInstance
import hu.attila.varga.weatherdemo.utils.network.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormatSymbols
import java.util.*
import kotlin.math.roundToInt

class MainActivityViewModel(val app: Application) : AndroidViewModel(app) {

    var currentData = MutableLiveData<CurrentData>()
    var forecastList = MutableLiveData<List<ForecastItemData>>()
    private var service: WeatherService = getRetrofitInstance().create(WeatherService::class.java)
    val dayNames: Array<String> = DateFormatSymbols(Locale.ENGLISH).weekdays
    var shortDayNames: Array<String> = DateFormatSymbols(Locale.ENGLISH).shortWeekdays
    val today: Calendar = Calendar.getInstance()

    init {
        getCurrentWeather()
        getForecast()
    }

    fun getCurrentWeather() {
        val lastCoord: Coord = PreferenceHelper(app).getLastCoord()
        service.getWeather(lastCoord.lat, lastCoord.lon, OPEN_WEATHER_MAP_API_KEY)
            .enqueue(object : Callback<CurrentWeatherResponse> {
                override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                    Toast.makeText(
                        app,
                        "ERROR during request: " + t.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<CurrentWeatherResponse>,
                    response: Response<CurrentWeatherResponse>
                ) {
                    if (response.body() != null) {

                        val data = CurrentData(
                            dayNames[today[Calendar.DAY_OF_WEEK]],
                            response.body()!!.name,
                            response.body()!!.weather[0].description,
                            response.body()!!.weather[0].icon,
                            response.body()!!.main.humidity,
                            (response.body()!!.main.temp - KELVIN).roundToInt(),
                            (response.body()!!.main.temp_min - KELVIN).roundToInt(),
                            (response.body()!!.main.temp_max - KELVIN).roundToInt(),
                            response.body()!!.main.pressure,
                            response.body()!!.wind.speed
                        )

                        currentData.postValue(data)
                    }
                }

            })
    }


    private fun getForecast() {
        val lastCoord: Coord = PreferenceHelper(app).getLastCoord()
        service.getForecast(lastCoord.lat, lastCoord.lon, EXCLUDE_VALUES, OPEN_WEATHER_MAP_API_KEY)
            .enqueue(object : Callback<ForecastResponse> {
                override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                    Toast.makeText(
                        app,
                        "ERROR during request: " + t.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<ForecastResponse>,
                    response: Response<ForecastResponse>
                ) {

                    val list = arrayListOf<ForecastItemData>()

                    if (response.body() != null) {
                        response.body()!!.daily.forEach {

                            val cal = GregorianCalendar.getInstance()
                            cal.timeInMillis = (it.sunrise * 1000).toLong()

                            list.add(
                                ForecastItemData(
                                    shortDayNames[cal[Calendar.DAY_OF_WEEK]].toUpperCase(Locale.getDefault()),
                                    it.weather[0].icon,
                                    (it.temp.min - KELVIN).roundToInt().toString() + DEGREE_SYMBOL,
                                    (it.temp.max - KELVIN).roundToInt().toString() + DEGREE_SYMBOL
                                )
                            )
                        }

                        forecastList.postValue(list)
                    }
                }

            })
    }
}
