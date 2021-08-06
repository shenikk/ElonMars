package com.example.elonmars.data.repository

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.WeatherItem
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Интерфейс репозитория по скачиванию данных из сети.
 */
interface IItemsRepository {

    /**
     * Метод для асинхронной загрузки информации о погоде.
     *
     * @return Single со списком моделей [WeatherItem]
     */
    fun loadDataAsync(): Single<ArrayList<WeatherItem>>

    /**
     * Метод для асинхронной загрузки списка фотографий.
     *
     * @return Single со списком моделей [PhotoItem]
     */
    fun loadPhotosAsync(): Single<ArrayList<PhotoItem>>
}