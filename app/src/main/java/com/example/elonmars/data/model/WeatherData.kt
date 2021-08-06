package com.example.elonmars

import com.google.gson.annotations.SerializedName

data class WeatherItem(
    var soles: ArrayList<Soles>)

data class Soles(@SerializedName("sol")
                 var weatherDay: String?,
                 @SerializedName("terrestrial_date")
                 var earthDate: String? = null,
                 @SerializedName("max_temp")
                 var highTemp: String? = null,
                 @SerializedName("min_temp")
                 var lowTemp: String? = null)