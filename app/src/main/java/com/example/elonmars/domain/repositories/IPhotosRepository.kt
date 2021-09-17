package com.example.elonmars.domain.repositories

import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.gallery.enums.GalleryType
import io.reactivex.Single

/** Интерфейс репозитория по скачиванию данных из сети. */
interface IPhotosRepository {

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
    fun getFavouritePhotosFromCache(): List<PhotoItem>

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
