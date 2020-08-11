package hu.attila.varga.weatherdemo.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import hu.attila.varga.weatherdemo.R
import hu.attila.varga.weatherdemo.data.model.current.Coord
import hu.attila.varga.weatherdemo.data.model.current.CurrentData
import hu.attila.varga.weatherdemo.data.model.current.CurrentWeatherResponse
import hu.attila.varga.weatherdemo.data.model.forecast.ForecastItemData
import hu.attila.varga.weatherdemo.data.model.forecast.ForecastResponse
import hu.attila.varga.weatherdemo.data.model.progress.ProgressData
import hu.attila.varga.weatherdemo.data.remote.RetrofitInstance
import hu.attila.varga.weatherdemo.data.remote.WeatherService
import hu.attila.varga.weatherdemo.utils.Utils
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormatSymbols
import java.util.*
import kotlin.math.roundToInt


class Repository(val app: Application) {

    private val Context.isConnected: Boolean
        get() {
            return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnected == true
        }

    var progress = MutableLiveData<ProgressData>()
    var currentWeatherLiveData = MutableLiveData<CurrentData>()
    var forecastListLiveData = MutableLiveData<List<ForecastItemData>>()

    val dayNames: Array<String> = DateFormatSymbols(Locale.ENGLISH).weekdays
    var shortDayNames: Array<String> = DateFormatSymbols(Locale.ENGLISH).shortWeekdays
    val today: Calendar = Calendar.getInstance()
    private var service: WeatherService = RetrofitInstance.getRetrofitInstance().create(
        WeatherService::class.java
    )

    fun getAllData(coordinates: Coord?) {
        if (coordinates != null) {
            progress.postValue(ProgressData(message = app.getString(R.string.downloading_data)))
            GlobalScope.launch {
                supervisorScope {
                    async { getCurrentWeather(coordinates) }
                    async { getForecast(coordinates) }
                    delay(1000)
                    progress.postValue(ProgressData(showProgress = false))
                }
            }
        }
    }


    private fun getCurrentWeather(coord: Coord) {
        if (app.isConnected) {
            service.getWeather(coord.lat, coord.lon, Utils.OPEN_WEATHER_MAP_API_KEY)
                .enqueue(object : Callback<CurrentWeatherResponse> {
                    override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                        Toast.makeText(
                            app,
                            app.getString(R.string.error_during_request) + t.localizedMessage,
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
                            // TODO: save new data to local storage
                            currentWeatherLiveData.postValue(data)
                        }
                    }
                })
        } else {
            // TODO get last data from local storage
        }
    }

    private fun getForecast(coord: Coord) {
        if (app.isConnected) {
            service.getForecast(
                coord.lat, coord.lon,
                Utils.EXCLUDE_VALUES,
                Utils.OPEN_WEATHER_MAP_API_KEY
            )
                .enqueue(object : Callback<ForecastResponse> {
                    override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                        Toast.makeText(
                            app,
                            app.getString(R.string.error_during_request) + t.localizedMessage,
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
                            // TODO: save new data to local storage
                        }
                    }
                })
        } else {
            // TODO get last data from local storage
        }
    }


}