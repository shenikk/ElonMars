package com.example.elonmars.domain.interactors

import com.example.elonmars.WeatherDataItem
import io.reactivex.Single

interface IWeatherInteractor {
    fun loadDataAsync(): Single<List<WeatherDataItem>>
    fun loadDataAsyncOnCall(): Single<List<WeatherDataItem>>
}
