package hu.attila.varga.weatherdemo.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hu.attila.varga.weatherdemo.R
import hu.attila.varga.weatherdemo.data.model.forecast.ForecastItemData
import hu.attila.varga.weatherdemo.utils.Utils
import kotlinx.android.synthetic.main.bottom_item_layout.view.*

class BottomRecyclerViewAdapter(val data: List<ForecastItemData>) :
    RecyclerView.Adapter<ForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.bottom_item_layout, parent, false)
        return ForecastViewHolder(
            listItem
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(data[position])
    }

}

class ForecastViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(forecastItemData: ForecastItemData) {
        view.bottom_item_day_name.text = forecastItemData.dayName
        view.bottom_item_temp_min.text = forecastItemData.tempMin.toString()
        view.bottom_item_temp_max.text = forecastItemData.tempMax.toString()
        Picasso.get()
            .load(Utils.IMAGE_BASE_URL + forecastItemData.weatherImage + "@4x.png")
            .into(view.bottom_item_image)
    }
}