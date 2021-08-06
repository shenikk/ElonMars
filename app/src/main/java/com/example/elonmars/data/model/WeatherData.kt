package com.example.elonmars

import com.google.gson.annotations.SerializedName

/**
 * Модель списка дней с информацией о погоде.
 *
 * @param weatherItems список дней с информацией о погоде
 */
data class WeatherData(
    var weatherItems: ArrayList<WeatherItem>)

/**
 * Модель, для описания данных о погоде за конкретный день.
 *
 * @param weatherDay дата на Марсе
 * @param earthDate дата на Земле
 * @param highTemp максимальная температура
 * @param lowTemp минимальная температура
 */
data class WeatherItem(@SerializedName("sol")
                 var weatherDay: String?,
                 @SerializedName("terrestrial_date")
                 var earthDate: String? = null,
                 @SerializedName("max_temp")
                 var highTemp: String? = null,
                 @SerializedName("min_temp")
                 var lowTemp: String? = null)