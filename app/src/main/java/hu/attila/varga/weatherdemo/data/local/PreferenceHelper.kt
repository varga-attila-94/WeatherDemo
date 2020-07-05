package hu.attila.varga.weatherdemo.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import hu.attila.varga.weatherdemo.data.model.current.Coord
import hu.attila.varga.weatherdemo.utils.Utils.Companion.DEFAULT_BUDAPEST_LAT
import hu.attila.varga.weatherdemo.utils.Utils.Companion.DEFAULT_BUDAPEST_LON
import hu.attila.varga.weatherdemo.utils.Utils.Companion.LAT_LON_PREF_KEY
import hu.attila.varga.weatherdemo.utils.Utils.Companion.PREF_NAME

class PreferenceHelper(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getPrefs(): SharedPreferences {
        return sharedPref
    }

    fun saveCurrentCoord(coord: Coord) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(LAT_LON_PREF_KEY, Gson().toJson(coord))
        editor.apply()
    }

    fun getLastCoord(): Coord {
        return Gson().fromJson(
            sharedPref.getString(
                LAT_LON_PREF_KEY,
                Gson().toJson(Coord(DEFAULT_BUDAPEST_LAT, DEFAULT_BUDAPEST_LON))
            ), Coord::class.java
        )
    }

}