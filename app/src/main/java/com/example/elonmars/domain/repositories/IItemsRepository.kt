package com.example.elonmars.domain.repositories

import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem
import io.reactivex.Single

/**
 * Интерфейс репозитория по скачиванию данных из сети.
 */
interface IItemsRepository {

    /**
     * Метод для асинхронной загрузки информации о погоде.
     *
     * @return Single со списком моделей [WeatherDataItem]
     */
    fun loadDataAsync(): Single<ArrayList<WeatherDataItem>>

    fun loadDataAsyncOnCall(): Single<ArrayList<WeatherDataItem>>
}