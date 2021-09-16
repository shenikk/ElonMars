package com.example.elonmars.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R
import com.example.elonmars.presentation.model.WeatherItem

/**
 * Адаптер для отображения списка погодных дней.
 *
 * @param dataSet список погодных дней [WeatherItem].
 */
class WeatherAdapter(private val dataSet: List<WeatherItem>) :
    RecyclerView.Adapter<WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = dataSet[position]
        holder.configureHolder(currentItem)
    }

    override fun getItemCount(): Int = dataSet.size
}
