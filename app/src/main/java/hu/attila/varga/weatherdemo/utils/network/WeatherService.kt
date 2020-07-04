package hu.attila.varga.weatherdemo.utils.network

import hu.attila.varga.weatherdemo.model.current.CurrentWeatherResponse
import hu.attila.varga.weatherdemo.model.forecast.ForecastResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apikey: String
    ): Call<CurrentWeatherResponse>

    @GET("/data/2.5/onecall")
    fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") apikey: String
    ): Call<ForecastResponse>

}