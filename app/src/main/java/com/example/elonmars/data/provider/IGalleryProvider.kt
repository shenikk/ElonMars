package com.example.elonmars.data.provider

import android.widget.ImageView
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
    fun loadPhotoItemsList(): ArrayList<PhotoItem>

    fun loadPhoto(view: ImageView, image: String)
}
