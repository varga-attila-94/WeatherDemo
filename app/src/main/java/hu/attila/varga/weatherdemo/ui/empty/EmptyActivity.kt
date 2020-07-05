package hu.attila.varga.weatherdemo.ui.empty

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import hu.attila.varga.weatherdemo.R
import hu.attila.varga.weatherdemo.databinding.SecondActivityBinding
import hu.attila.varga.weatherdemo.ui.base.BaseActivity

class EmptyActivity : BaseActivity() {

    private lateinit var viewModel: EmptyActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCurrentMenuItem(1)
        viewModel = ViewModelProvider(this).get(EmptyActivityViewModel::class.java)
        val binding: SecondActivityBinding = putContentView(R.layout.second_activity)
        binding.lifecycleOwner = this
        binding.apply {
            secondViewModel = viewModel
        }
        setProgressVisibility(false)

        setSwipeRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            swipeContainer.isRefreshing = false
        })
    }

}


