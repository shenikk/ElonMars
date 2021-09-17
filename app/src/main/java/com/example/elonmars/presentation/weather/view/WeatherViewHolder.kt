package com.example.elonmars.presentation.weather.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R
import com.example.elonmars.presentation.weather.model.WeatherItem

/**
 * ViewHolder для [com.example.elonmars.presentation.adapter.WeatherAdapter]
 */
class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val weatherDay: TextView = view.findViewById(R.id.weather_day)
    private val earthDay: TextView = view.findViewById(R.id.earth_day)
    private val tempHigh: TextView = view.findViewById(R.id.temp_high)
    private val tempLow: TextView = view.findViewById(R.id.temp_low)

    fun configureHolder(currentItem: WeatherItem) {
        weatherDay.text = currentItem.weatherDay
        earthDay.text = currentItem.earthDate
        tempHigh.text = currentItem.highTemp
        tempLow.text = currentItem.lowTemp
    }
}
