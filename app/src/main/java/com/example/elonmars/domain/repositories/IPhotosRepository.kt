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
    fun loadPhotosAsync(): Observable<List<PhotoItem>>

    fun loadPhotosOnCall(): Single<List<PhotoItem>>

    fun getPhotosFromCache(): List<PhotoItem>

    fun setFavourite(photoItem: PhotoItem)
}
