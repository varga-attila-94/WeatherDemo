package hu.attila.varga.weatherdemo.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import hu.attila.varga.weatherdemo.R
import hu.attila.varga.weatherdemo.databinding.ActivityBaseBinding
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