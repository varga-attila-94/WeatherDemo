package hu.attila.varga.weatherdemo.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import hu.attila.varga.weatherdemo.data.Repository
import hu.attila.varga.weatherdemo.ui.detail.DetailFragment

class DetailsPagerAdapter(fragmentManager: FragmentManager, private val repository: Repository) :
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return DetailFragment(repository, position)
    }

    override fun getCount(): Int {
        return 2
    }
}