package com.example.elonmars.presentation.detailphoto.viewmodel

import androidx.lifecycle.ViewModel
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.provider.ISchedulersProvider
import com.example.elonmars.domain.interactors.IPhotosInteractor
import com.example.elonmars.presentation.extensions.logDebug

/**
 * ViewModel экрана со списком фильмов.
 *
 * @param photosInteractor интерактор, обрабатывающий данные с фото.
 * @param schedulersProvider провайдер с Scheduler для работы на разных потоках.
 *
 * @testClass unit: DetailPhotoViewModelTest
 */
class DetailPhotoViewModel(
    private val photosInteractor: IPhotosInteractor,
    private val schedulersProvider: ISchedulersProvider
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        logDebug("onCleared() called")
    }

    /** Устанавливает значение фото, как 'любимое' */
    fun setFavourite(photoItem: PhotoItem) {
        photosInteractor.setFavourite(photoItem)
    }
}
