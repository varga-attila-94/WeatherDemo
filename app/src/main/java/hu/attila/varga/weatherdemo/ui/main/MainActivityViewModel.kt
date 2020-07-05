package hu.attila.varga.weatherdemo.ui.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import hu.attila.varga.weatherdemo.data.model.current.Coord
import hu.attila.varga.weatherdemo.data.model.current.CurrentData
import hu.attila.varga.weatherdemo.data.model.current.CurrentWeatherResponse
import hu.attila.varga.weatherdemo.data.model.forecast.ForecastItemData
import hu.attila.varga.weatherdemo.data.model.forecast.ForecastResponse
import hu.attila.varga.weatherdemo.data.local.PreferenceHelper
import hu.attila.varga.weatherdemo.data.local.SharedPreferenceLiveData
import hu.attila.varga.weatherdemo.utils.Utils
import hu.attila.varga.weatherdemo.utils.Utils.Companion.DEGREE_SYMBOL
import hu.attila.varga.weatherdemo.utils.Utils.Companion.EXCLUDE_VALUES
import hu.attila.varga.weatherdemo.utils.Utils.Companion.KELVIN
import hu.attila.varga.weatherdemo.utils.Utils.Companion.LAT_LON_PREF_KEY
import hu.attila.varga.weatherdemo.utils.Utils.Companion.OPEN_WEATHER_MAP_API_KEY
import hu.attila.varga.weatherdemo.data.remote.RetrofitInstance.Companion.getRetrofitInstance
import hu.attila.varga.weatherdemo.data.remote.WeatherService
import hu.attila.varga.weatherdemo.data.local.stringLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormatSymbols
import java.util.*
import kotlin.math.roundToInt

class MainActivityViewModel(val app: Application) : AndroidViewModel(app) {

    var currentLiveData = MutableLiveData<CurrentData>()
    var forecastListLiveData = MutableLiveData<List<ForecastItemData>>()
    var currentLocationLiveData: SharedPreferenceLiveData<String>

    private var service: WeatherService = getRetrofitInstance().create(
        WeatherService::class.java)
    val dayNames: Array<String> = DateFormatSymbols(Locale.ENGLISH).weekdays
    var shortDayNames: Array<String> = DateFormatSymbols(Locale.ENGLISH).shortWeekdays
    val today: Calendar = Calendar.getInstance()


    init {
        getCurrentWeather()
        getForecast()

        currentLocationLiveData = PreferenceHelper(
            app
        ).getPrefs().stringLiveData(LAT_LON_PREF_KEY, Gson().toJson(Coord(
            Utils.DEFAULT_BUDAPEST_LAT,
            Utils.DEFAULT_BUDAPEST_LON
        )))
    }

    fun getCurrentWeather() {
        val lastCoord: Coord = PreferenceHelper(
            app
        ).getLastCoord()
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

                        currentLiveData.postValue(data)
                    }
                }

            })
    }


    fun getForecast() {
        val lastCoord: Coord = PreferenceHelper(
            app
        ).getLastCoord()
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

                        forecastListLiveData.postValue(list)
                    }
                }

            })
    }
}
