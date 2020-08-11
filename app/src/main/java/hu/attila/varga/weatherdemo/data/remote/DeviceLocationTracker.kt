package hu.attila.varga.weatherdemo.data.remote

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import hu.attila.varga.weatherdemo.data.model.current.Coord
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class DeviceLocationTracker(context: Context, deviceLocationListener: DeviceLocationListener) :
    LocationListener, CoroutineScope {
    private var deviceLocation: Location? = null
    private val context: WeakReference<Context>
    private var locationManager: LocationManager? = null
    private var deviceLocationListener: DeviceLocationListener
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    init {
        this.context = WeakReference(context)
        this.deviceLocationListener = deviceLocationListener
        initializeLocationProviders()
    }

    private fun initializeLocationProviders() {
        //Init Location Manger if not already initialized
        if (null == locationManager) {
            locationManager = context.get()
                ?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        locationManager?.apply {
            // flag for GPS status
            val isGPSEnabled =
                isProviderEnabled(LocationManager.GPS_PROVIDER)            // flag for network status
            val isNetworkEnabled =
                isProviderEnabled(LocationManager.PASSIVE_PROVIDER)            //If we have permission
            if (ActivityCompat.checkSelfPermission(
                    context.get()!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context.get()!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {                //First Try GPS
                if (isGPSEnabled) {
                    requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        UPDATE_FREQUENCY_TIME,
                        UPDATE_FREQUENCY_DISTANCE.toFloat(), this@DeviceLocationTracker
                    )
                    deviceLocation =
                        locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } else {
                    // Show alert to open GPS
                    context.get()?.apply {
                        AlertDialog.Builder(this)
                            .setTitle("Enable GPS")
                            .setMessage("Enable GPS")
                            .setPositiveButton(
                                "Settings"
                            ) { _, _ ->
                                val intent = Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                                )
                                startActivity(intent)
                            }.setNegativeButton("Cancel")
                            { dialog, _ -> dialog.cancel() }.show()
                    }
                }                //If failed try using NetworkManger
                if (null == deviceLocation && isNetworkEnabled) {
                    requestLocationUpdates(
                        LocationManager.PASSIVE_PROVIDER,
                        0, 0f,
                        this@DeviceLocationTracker
                    )
                    deviceLocation =
                        locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
            }
        }
    }

    /**
     * Stop using GPS listener
     * Must call this function to stop using GPS
     */
    fun stopUpdate() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@DeviceLocationTracker)
        }
    }

    override fun onLocationChanged(newDeviceLocation: Location) {
        deviceLocation = newDeviceLocation
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                deviceLocationListener.onDeviceLocationChanged(
                    Coord(
                        newDeviceLocation.latitude,
                        newDeviceLocation.longitude
                    )
                )
            }
        }
    }

    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    interface DeviceLocationListener {
        fun onDeviceLocationChanged(coord: Coord?)
    }

    companion object {
        // The minimum distance to change Updates in meters
        private const val UPDATE_FREQUENCY_DISTANCE: Long =
            10 // 10 meters

        // The minimum time between updates in milliseconds
        private const val UPDATE_FREQUENCY_TIME: Long = 10000 // 1 minute
        private val TAG = DeviceLocationTracker::class.java.simpleName
    }
}