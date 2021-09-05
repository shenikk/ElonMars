package com.example.elonmars.domain.repositories

import android.widget.ImageView
import com.example.elonmars.data.model.PhotoItem
import io.reactivex.Completable
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

    fun getPhotosFromCache(): ArrayList<PhotoItem>

    fun setFavourite(photoItem: PhotoItem)

    fun loadPhoto(view: ImageView, image: String)
}
