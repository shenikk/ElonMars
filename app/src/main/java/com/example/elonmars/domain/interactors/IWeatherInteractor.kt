package com.example.elonmars.domain.interactors

import com.example.elonmars.presentation.model.WeatherItem
import io.reactivex.Single

interface IWeatherInteractor {
    fun loadDataAsync(): Single<Unit>
    fun loadDataAsyncOnCall(): Single<Unit>
    fun convertTempreature()
    fun getWeatherItems(): List<WeatherItem>?
    fun getLatestWeatherDay(): WeatherItem?
}
