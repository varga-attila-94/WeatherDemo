package hu.attila.varga.weatherdemo.data

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import hu.attila.varga.weatherdemo.data.local.PreferenceHelper
import hu.attila.varga.weatherdemo.data.local.SharedPreferenceLiveData
import hu.attila.varga.weatherdemo.data.local.stringLiveData
import hu.attila.varga.weatherdemo.data.model.current.Coord
import hu.attila.varga.weatherdemo.data.model.current.CurrentData
import hu.attila.varga.weatherdemo.data.model.current.CurrentWeatherResponse
import hu.attila.varga.weatherdemo.data.model.forecast.ForecastItemData
import hu.attila.varga.weatherdemo.data.model.forecast.ForecastResponse
import hu.attila.varga.weatherdemo.data.remote.RetrofitInstance
import hu.attila.varga.weatherdemo.data.remote.WeatherService
import hu.attila.varga.weatherdemo.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormatSymbols
import java.util.*
import kotlin.math.roundToInt

class Repository(val app: Application) {

    var currentWeatherLiveData = MutableLiveData<CurrentData>()
    var forecastListLiveData = MutableLiveData<List<ForecastItemData>>()

    val dayNames: Array<String> = DateFormatSymbols(Locale.ENGLISH).weekdays
    var shortDayNames: Array<String> = DateFormatSymbols(Locale.ENGLISH).shortWeekdays
    val today: Calendar = Calendar.getInstance()
    private var service: WeatherService

    init {
        service = RetrofitInstance.getRetrofitInstance().create(
            WeatherService::class.java
        )
    }

    var preferenceHelper: PreferenceHelper = PreferenceHelper((app))


    fun getCurrentWeather(): MutableLiveData<CurrentData> {

        val lastCoord: Coord = preferenceHelper.getLastCoord()
        service.getWeather(lastCoord.lat, lastCoord.lon, Utils.OPEN_WEATHER_MAP_API_KEY)
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
                            (response.body()!!.main.temp - Utils.KELVIN).roundToInt(),
                            (response.body()!!.main.temp_min - Utils.KELVIN).roundToInt(),
                            (response.body()!!.main.temp_max - Utils.KELVIN).roundToInt(),
                            response.body()!!.main.pressure,
                            response.body()!!.wind.speed
                        )
                        currentWeatherLiveData.postValue(data)

                    }
                }
            })
        return currentWeatherLiveData
    }

    fun getForecast(): MutableLiveData<List<ForecastItemData>> {
        val lastCoord: Coord = PreferenceHelper(
            app
        ).getLastCoord()
        service.getForecast(
            lastCoord.lat, lastCoord.lon,
            Utils.EXCLUDE_VALUES,
            Utils.OPEN_WEATHER_MAP_API_KEY
        )
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
                                    (it.temp.min - Utils.KELVIN).roundToInt()
                                        .toString() + Utils.DEGREE_SYMBOL,
                                    (it.temp.max - Utils.KELVIN).roundToInt()
                                        .toString() + Utils.DEGREE_SYMBOL
                                )
                            )
                        }
                        forecastListLiveData.postValue(list)
                    }
                }

            })
        return forecastListLiveData
    }

    fun getCurrentLocation(): SharedPreferenceLiveData<String> {
        return PreferenceHelper(
            app
        ).getPrefs().stringLiveData(
            Utils.LAT_LON_PREF_KEY, Gson().toJson(
                Coord(
                    Utils.DEFAULT_BUDAPEST_LAT,
                    Utils.DEFAULT_BUDAPEST_LON
                )
            )
        )
    }
}