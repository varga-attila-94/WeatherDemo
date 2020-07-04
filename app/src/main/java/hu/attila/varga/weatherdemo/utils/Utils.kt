package hu.attila.varga.weatherdemo.utils

import hu.attila.varga.weatherdemo.BuildConfig

class Utils {

    companion object {
        const val OPEN_WEATHER_MAP_API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY
        const val BASE_URL = "https://api.openweathermap.org/"
        const val IMAGE_BASE_URL = "http://openweathermap.org/img/wn/"
        const val EXCLUDE_VALUES = "current,minutely,hourly"
        const val LAT = 47.496130
        const val LON = 19.050605

        const val KELVIN = 273.15
        const val DEGREE_SYMBOL = "°"
    }
}

