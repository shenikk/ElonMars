package com.example.elonmars.data.repository

import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.provider.IWeatherItemProvider
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.repositories.IItemsRepository
import io.reactivex.Single

/**
 * Репозиторий - провайдер данных о скачанной информации по фото и погоде.
 *
 * @param dataStorage хранилище данных
 * @param weatherItemsProvider провайдер погодных данных
 * @param galleryProvider провайдер данных с фото
 */
class ItemsRepository(
    private val dataStorage: IDataStorage,
    private val weatherItemsProvider: IWeatherItemProvider
) : IItemsRepository {

    override fun loadDataAsync(): Single<ArrayList<WeatherDataItem>> {
        return Single.fromCallable {
            dataStorage.weatherDataItems
                ?: ArrayList(weatherItemsProvider.loadWeatherItemsList().take(10))
                    .also { dataStorage.weatherDataItems = it }
        }
    }

    override fun loadDataAsyncOnCall(): Single<ArrayList<WeatherDataItem>> {
        return Single.fromCallable {
            ArrayList(weatherItemsProvider.loadWeatherItemsList().take(10))
                .also { dataStorage.weatherDataItems = it }
        }
    }
}