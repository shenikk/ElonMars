package com.example.elonmars

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter(private val dataSet: ArrayList<Soles>) : RecyclerView.Adapter<WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = dataSet[position]

        holder.weatherDay.text = currentItem.weatherDay
        holder.earthDay.text = currentItem.earthDate
        holder.tempHigh.text = currentItem.highTemp
        holder.tempLow.text = currentItem.lowTemp
    }

    override fun getItemCount(): Int = dataSet.size
}