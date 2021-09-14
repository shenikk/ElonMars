package com.example.elonmars.domain.interactors

import com.example.elonmars.WeatherDataItem
import com.example.elonmars.presentation.model.WeatherItem
import io.reactivex.Single

interface IWeatherInteractor {
    fun loadDataAsync(): Single<List<WeatherDataItem>>
    fun loadDataAsyncOnCall(): Single<List<WeatherDataItem>>
    fun convertTempreature()
    fun doOnSuccess(weatherDataItemList: List<WeatherDataItem>)
    fun getWeatherItems(): List<WeatherItem>?
    fun getLatestWeatherDay(): WeatherItem?
}
