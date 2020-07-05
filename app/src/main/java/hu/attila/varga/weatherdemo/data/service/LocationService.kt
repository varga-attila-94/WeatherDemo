package hu.attila.varga.weatherdemo.data.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import hu.attila.varga.weatherdemo.data.local.PreferenceHelper
import hu.attila.varga.weatherdemo.data.model.current.Coord

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastKnownLocation()
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(
                        "FFF",
                        location.latitude.toString() + " , " + location.longitude.toString()
                    )

                    PreferenceHelper(this@LocationService)
                        .saveCurrentCoord(
                            Coord(
                                location.latitude,
                                location.longitude
                            )
                        )
                }

            }
    }


}
