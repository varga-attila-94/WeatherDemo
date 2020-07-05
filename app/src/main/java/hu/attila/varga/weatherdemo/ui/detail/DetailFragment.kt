package hu.attila.varga.weatherdemo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import hu.attila.varga.weatherdemo.R
import hu.attila.varga.weatherdemo.data.Repository
import hu.attila.varga.weatherdemo.databinding.DetailFragmentBinding

class DetailFragment(val repository: Repository, val position: Int) : Fragment() {

    lateinit var binding: DetailFragmentBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, DetailFragmentViewModelFactory(repository))
            .get(DetailViewModel::class.java)
        binding = DataBindingUtil.inflate<DetailFragmentBinding>(
            inflater,
            R.layout.detail_fragment, container, false
        )
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            val fragmentData = viewModel.getFragmentDataByPosition(position)

            value1.text = fragmentData[0].value
            name1.text = fragmentData[0].name
            unit1.text = fragmentData[0].unit

            value2.text = fragmentData[1].value
            name2.text = fragmentData[1].name
            unit2.text = fragmentData[1].unit

            value3.text = fragmentData[2].value
            name3.text = fragmentData[2].name
            unit3.text = fragmentData[2].unit

        }
    }

}