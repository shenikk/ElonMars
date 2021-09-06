package com.example.elonmars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.data.provider.ISchedulersProvider
import com.example.elonmars.domain.interactors.IPhotosInteractor
import com.example.elonmars.presentation.extensions.logDebug

/**
 * ViewModel экрана со списком фильмов.
 *
 * @param itemsRepository репозиторий с данными о фото
 * @param schedulersProvider
 *
 * @testClass unit: GalleryViewModelTest
 */
class DetailPhotoViewModel(
    private val photosInteractor: IPhotosInteractor,
    private val schedulersProvider: ISchedulersProvider
) : ViewModel() {

    fun setFavourite(photoItem: PhotoItem) {
        photosInteractor.setFavourite(photoItem)
    }

    override fun onCleared() {
        super.onCleared()
        logDebug("onCleared() called")
    }
}
