package com.example.elonmars.domain.repositories

import com.example.elonmars.data.model.PhotoItem
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
    fun loadPhotosAsync(): Single<ArrayList<PhotoItem>>

    fun loadPhotosOnCall(): Single<ArrayList<PhotoItem>>
}
