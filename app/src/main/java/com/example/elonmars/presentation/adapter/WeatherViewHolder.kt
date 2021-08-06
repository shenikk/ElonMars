package com.example.elonmars.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R

/**
 * ViewHolder для [com.example.elonmars.presentation.adapter.WeatherViewHolder]
 */
class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var weatherDay: TextView = view.findViewById(R.id.weather_day)
    var earthDay: TextView = view.findViewById(R.id.earth_day)
    var tempHigh: TextView = view.findViewById(R.id.temp_high)
    var tempLow: TextView = view.findViewById(R.id.temp_low)
}