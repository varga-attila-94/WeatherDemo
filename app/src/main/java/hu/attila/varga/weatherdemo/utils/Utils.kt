package hu.attila.varga.weatherdemo.utils

import hu.attila.varga.weatherdemo.BuildConfig

class Utils {

    companion object {
        const val OPEN_WEATHER_MAP_API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY
        const val BASE_URL = "https://api.openweathermap.org/"
        const val IMAGE_BASE_URL = "http://openweathermap.org/img/wn/"
        const val EXCLUDE_VALUES = "current,minutely,hourly"
        const val PREF_NAME = "pref_name"
        const val LAT_LON_PREF_KEY = "lat_lon_pref_key"

        const val DEFAULT_BUDAPEST_LAT = 47.496130
        const val DEFAULT_BUDAPEST_LON = 19.050605

        const val KELVIN = 273.15
        const val DEGREE_SYMBOL = "°"
    }
}

