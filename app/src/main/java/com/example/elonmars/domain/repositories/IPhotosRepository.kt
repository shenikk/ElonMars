package com.example.elonmars.domain.repositories

import com.example.elonmars.data.model.PhotoItem
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Интерфейс репозитория по скачиванию данных из сети.
 */
interface IPhotosRepository {

    /**
     * Метод для асинхронной загрузки списка фотографий.
     *
     * @return Single со списком моделей [PhotoItem]
     */
    fun loadPhotosAsync(): Single<List<PhotoItem>>

    fun loadPhotosOnCall(): Single<List<PhotoItem>>

    fun getFavouritePhotosFromCache(): List<PhotoItem>

    fun setFavourite(photoItem: PhotoItem)
}
