package com.example.elonmars.data.model

import com.google.gson.annotations.SerializedName

/**
 * Модель списка дней с информацией о погоде.
 *
 * @param weatherDataItems список дней с информацией о погоде
 */
data class WeatherData(
    @SerializedName("soles")
    var weatherDataItems: List<WeatherDataItem>)

/**
 * Модель, для описания данных о погоде за конкретный день.
 *
 * @param weatherDay дата на Марсе
 * @param earthDate дата на Земле
 * @param highTemp максимальная температура
 * @param lowTemp минимальная температура
 */
data class WeatherDataItem(
                 @SerializedName("sol")
                 var weatherDay: String?,
                 @SerializedName("terrestrial_date")
                 var earthDate: String? = null,
                 @SerializedName("max_temp")
                 var highTemp: String? = null,
                 @SerializedName("min_temp")
                 var lowTemp: String? = null)
