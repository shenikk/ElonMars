package com.example.elonmars.domain.interactors

import com.example.elonmars.WeatherDataItem
import com.example.elonmars.domain.repositories.IItemsRepository
import io.reactivex.Single

class WeatherInteractor(private val itemsRepository: IItemsRepository) : IWeatherInteractor {

    override fun loadDataAsync(): Single<ArrayList<WeatherDataItem>> {
        return itemsRepository.loadDataAsync()
    }

    override fun loadDataAsyncOnCall(): Single<ArrayList<WeatherDataItem>> {
        return itemsRepository.loadDataAsyncOnCall()
    }
}
