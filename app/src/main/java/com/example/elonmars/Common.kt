package com.example.elonmars

import com.example.elonmars.retrofit.ApiInterface
import com.example.elonmars.retrofit.RetrofitClient
import com.example.elonmars.retrofit.WeatherApiInterface

const val PHOTOS_BASE_URL = "https://api.nasa.gov/"
const val WEATHER_BASE_URL = "https://mars.nasa.gov/"
const val API_KEY = "S0pO0YrtiG1iqfuIokCyaD1xM0D4xob8ywxlM6uf"

object Common {
    val retrofit: ApiInterface
        get() = RetrofitClient.getRetrofit(PHOTOS_BASE_URL).create(ApiInterface::class.java)

    val weatherRetrofit: WeatherApiInterface
        get() = RetrofitClient.getWeatherRetrofit(WEATHER_BASE_URL).create(WeatherApiInterface::class.java)
}