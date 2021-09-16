package com.example.elonmars.domain.repositories

import com.example.elonmars.data.model.WeatherDataItem
import io.reactivex.Single

/**
 * Интерфейс репозитория по скачиванию данных из сети.
 */
interface IItemsRepository {

    /**
     * Метод для асинхронной загрузки информации о погоде.
     *
     * @return Single со списком моделей [WeatherDataItem].
     */
    fun loadDataAsync(): Single<List<WeatherDataItem>>

    /**
     * Метод для асинхронной загрузки информации о погоде по действию пользователя pull to refresh.
     *
     * @return Single со списком моделей [WeatherDataItem].
     */
    fun loadDataAsyncOnCall(): Single<List<WeatherDataItem>>
}
