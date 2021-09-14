package com.example.elonmars.domain.provider

import com.example.elonmars.data.model.PhotoItem

/**
 * Интерфейс провайдера данных о загруженных из сети фото.
 */
interface IGalleryProvider {

    /**
     * Метод для загрузки списка моделей типа [PhotoItem].
     *
     * @return список моделей [PhotoItem] описывающих загруженные из сети фото на космическую тему.
     */
    fun loadPhotoItemsList(): List<PhotoItem>
}
