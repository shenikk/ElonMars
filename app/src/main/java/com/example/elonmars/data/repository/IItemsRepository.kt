package com.example.elonmars.data.repository

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.WeatherDataItem
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

    /**
     * Метод для асинхронной загрузки списка фотографий.
     *
     * @return Single со списком моделей [PhotoItem]
     */
    fun loadPhotosAsync(): Single<ArrayList<PhotoItem>>
}