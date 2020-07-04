package hu.attila.varga.weatherdemo.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import hu.attila.varga.weatherdemo.R
import hu.attila.varga.weatherdemo.databinding.ActivityMainBinding
import hu.attila.varga.weatherdemo.utils.Utils.Companion.IMAGE_BASE_URL
import hu.attila.varga.weatherdemo.view.adapter.BottomRecyclerViewAdapter
import hu.attila.varga.weatherdemo.viewmodel.MainActivityViewModel
import hu.attila.varga.weatherdemo.viewmodel.MainActivityViewModelFactory


class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var item: LinearLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCurrentMenuItem(0)
        viewModelFactory = MainActivityViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        val binding: ActivityMainBinding = putContentView(R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.apply {
            weatherViewModel = viewModel
        }


        viewModel.currentData.observe(this, Observer {
            swipeContainer.isRefreshing = false
            if (it != null) {
                Picasso.get()
                    .load(IMAGE_BASE_URL + it.weatherImage + getString(R.string.image_extension))
                    .into(findViewById<ImageView>(R.id.weather_image))
                setProgressVisibility(false)
            }
        })


        recyclerView = findViewById(R.id.bottom_recyclerview)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        viewModel.forecastList.observe(this, Observer {
            recyclerView.adapter = BottomRecyclerViewAdapter(it)
        })


        setSwipeRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if (true) { // TODO: Check internet connection
                viewModel.getCurrentWeather()
            } else {
                swipeContainer.isRefreshing = false
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


}


