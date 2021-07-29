package com.example.elonmars.retrofit

import com.example.elonmars.WeatherItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiInterface {

    @GET("rss/api")
    fun getWeatherData(
            @Query("feed") feed: String,
            @Query("category") category: String,
            @Query("feedtype") feedtype: String
    ) : Call<WeatherItem>
}