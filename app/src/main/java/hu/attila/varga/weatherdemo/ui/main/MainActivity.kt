package hu.attila.varga.weatherdemo.ui.main

import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.squareup.picasso.Picasso
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import hu.attila.varga.weatherdemo.R
import hu.attila.varga.weatherdemo.databinding.ActivityMainBinding
import hu.attila.varga.weatherdemo.ui.base.BaseActivity
import hu.attila.varga.weatherdemo.ui.main.adapter.BottomRecyclerViewAdapter
import hu.attila.varga.weatherdemo.ui.main.adapter.DetailsPagerAdapter
import hu.attila.varga.weatherdemo.utils.Utils.Companion.IMAGE_BASE_URL


class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: DetailsPagerAdapter
    private lateinit var dotsIndicator: SpringDotsIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCurrentMenuItem(0)
        viewModelFactory =
            MainActivityViewModelFactory(
                application
            )
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        val binding: ActivityMainBinding = putContentView(R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.apply {
            weatherViewModel = viewModel
        }

        viewModel.showProgressBar.observe(this, Observer {
            setProgressVisibility(it)
        })

        viewPager = findViewById(R.id.viewPager)
        dotsIndicator = findViewById(R.id.dots_indicator)


        viewModel.currentWeatherLiveData.observe(this, Observer {
            swipeContainer.isRefreshing = false
            if (it != null) {

                pagerAdapter = DetailsPagerAdapter(supportFragmentManager, viewModel.repository)
                viewPager.adapter = pagerAdapter
                dotsIndicator.setViewPager(viewPager)

                Picasso.get()
                    .load(IMAGE_BASE_URL + it.weatherImage + getString(R.string.image_extension))
                    .into(findViewById<ImageView>(R.id.weather_image))
            }
        })


        recyclerView = findViewById(R.id.bottom_recyclerview)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        viewModel.forecastListLiveData.observe(this, Observer {
            if (it != null) {
                recyclerView.adapter =
                    BottomRecyclerViewAdapter(
                        it
                    )
            }
        })

        viewModel.currentLocationLiveData.observe(this, Observer {
            if (it != null) {
                viewModel.getCurrentWeather()
                viewModel.getForecast()
            }
        })

        setSwipeRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            viewModel.getCurrentWeather()
            viewModel.getForecast()
        })

    }


}


