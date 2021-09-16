package com.example.elonmars.domain.interactors

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.GalleryType
import io.reactivex.Observable
import io.reactivex.Single

/** Интерфейс интерактора для экрана галереи. */
interface IPhotosInteractor {

    /**
     * Метод для асинхронной загрузки списка фотографий.
     *
     * @return Single со списком моделей [PhotoItem].
     */
    fun loadPhotosAsync(): Single<List<PhotoItem>>

    /**
     * Метод для асинхронной загрузки списка фотографий по действию пользователя pull to refresh.
     *
     * @return Single со списком моделей [PhotoItem].
     */
    fun loadPhotosOnCall(): Single<List<PhotoItem>>

    /**
     * Метод возвращает список 'любимых' фотографий из кэша.
     *
     * @return Список моделей [PhotoItem].
     */
    fun getFavouritePhotos(): List<PhotoItem>

    /**
     * Метод устанавливает значение фото, как 'любимое'.
     *
     * @param photoItem фото, которое нужно сделать 'любимым'.
     */
    fun setFavourite(photoItem: PhotoItem)

    /**
     * Метод устанавливает тип экрана галереи.
     * Состояние экрана представлено порядковым номером константы перечисления [GalleryType].
     */
    fun setGalleryType(type: Int)

    /**
     * Метод возвращет типа экрана галереи.
     * Состояние экрана представлено порядковым номером константы перечисления [GalleryType].
     */
    fun getGalleryType(): Int
}
