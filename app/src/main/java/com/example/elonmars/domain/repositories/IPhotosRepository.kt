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
    fun loadPhotosAsync(): Observable<ArrayList<PhotoItem>>

    fun loadPhotosOnCall(): Single<ArrayList<PhotoItem>>

    fun getPhotosFromCache(): ArrayList<PhotoItem>

    fun setFavourite(photoItem: PhotoItem)
}
