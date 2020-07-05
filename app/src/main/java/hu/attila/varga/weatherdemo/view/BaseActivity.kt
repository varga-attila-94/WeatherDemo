package hu.attila.varga.weatherdemo.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import hu.attila.varga.weatherdemo.R
import hu.attila.varga.weatherdemo.databinding.ActivityBaseBinding
import hu.attila.varga.weatherdemo.model.current.Coord
import hu.attila.varga.weatherdemo.utils.LocationService
import hu.attila.varga.weatherdemo.utils.PreferenceHelper
import hu.attila.varga.weatherdemo.utils.Utils.Companion.LAT_LON_PREF_KEY
import hu.attila.varga.weatherdemo.viewmodel.BaseActivityViewModel
import org.jetbrains.annotations.NotNull

abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var binding: ActivityBaseBinding
    lateinit var baseViewModel: BaseActivityViewModel
    lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var progressDialog: ProgressDialog
    val PERMISSION_ID = 442
    val serviceClass = LocationService::class.java
    lateinit var locationIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        baseViewModel = BaseActivityViewModel()
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_base
        )

        initLayout()

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        locationIntent = Intent(this@BaseActivity, serviceClass)
        getLastLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (!isServiceRunning(serviceClass)) {
                    startService(locationIntent)
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    // Custom method to determine whether a service is running
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }


    protected fun setSwipeRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener) {
        swipeContainer.setOnRefreshListener(listener)
    }


    protected open fun <T : @NotNull ViewDataBinding> putContentView(@LayoutRes resId: Int): T {
        baseViewModel.isProgressVisible.observe(this, Observer {
            if (it) progressDialog.show() else progressDialog.dismiss()
        })
        return DataBindingUtil.inflate(
            layoutInflater,
            resId,
            binding.layoutContainer,
            true,
            DataBindingUtil.getDefaultComponent()
        )
    }

    protected fun setProgressVisibility(value: Boolean) {
        baseViewModel.setVisibility(value)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.weather_page -> {
                Handler().postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 250)
            }
            R.id.empty_page -> {
                Handler().postDelayed({
                    startActivity(Intent(this, SecondActivity::class.java))
                    finish()
                }, 250)

            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun setCurrentMenuItem(index: Int) {
        navView.menu.getItem(index).isChecked = true
    }

    private fun initLayout() {
        // dark status bar text color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setColorSchemeResources(R.color.titleColor)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(getString(R.string.loading))
        progressDialog.setMessage(getString(R.string.please_wait))
    }
}