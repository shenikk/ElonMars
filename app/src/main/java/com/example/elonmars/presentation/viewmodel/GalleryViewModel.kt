package com.example.elonmars.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.domain.provider.ISchedulersProvider
import com.example.elonmars.domain.interactors.IPhotosInteractor
import com.example.elonmars.presentation.GalleryType
import com.example.elonmars.presentation.extensions.logDebug
import io.reactivex.disposables.Disposable

/**
 * ViewModel экрана со списком фильмов.
 *
 * @param itemsRepository репозиторий с данными о фото
 * @param schedulersProvider
 *
 * @testClass unit: GalleryViewModelTest
 */
class GalleryViewModel(
    private val photosInteractor: IPhotosInteractor,
    private val schedulersProvider: ISchedulersProvider
) : ViewModel() {

    private var disposable: Disposable? = null

    private val progressLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val photoItemsLiveData = MutableLiveData<List<PhotoItem>>()
    private val favPhotoItemsLiveData = MutableLiveData<List<PhotoItem>>()
    private val refreshLiveData = MutableLiveData<Boolean>()
    private val contentTypeLiveData = MutableLiveData<Int>()

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        logDebug("onCleared() called")
    }

    /**
     * Метод для асинхронной загрузки списка фото.
     */
    fun loadDataAsync() {
        disposable = photosInteractor.loadPhotosAsync()
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(photoItemsLiveData::setValue, errorLiveData::setValue)
    }

    /**
     * Метод для асинхронной загрузки списка фото после действия pull to refresh.
     */
    fun loadDataOnForceAsync() {
        disposable = photosInteractor.loadPhotosOnCall()
            .doOnSubscribe { refreshLiveData.postValue(true) }
            .doAfterTerminate { refreshLiveData.postValue(false) }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(photoItemsLiveData::setValue, errorLiveData::setValue)
    }

    fun getFavouritePhotos() {
        favPhotoItemsLiveData.value = photosInteractor.getFavouritePhotos()
    }

    fun setFavourite(photoItem: PhotoItem) {
        photosInteractor.setFavourite(photoItem)
        updateContent()
    }

    fun setContentType(galleryType: GalleryType) {
        photosInteractor.setGalleryType(galleryType.ordinal)
        contentTypeLiveData.value = galleryType.ordinal
    }

    fun getContentTypeLiveData(): LiveData<Int> {
        return contentTypeLiveData
    }

    /**
     * Метод для получения инстанса LiveData
     *
     * @return LiveData с [Boolean]
     */
    fun getProgressLiveData(): LiveData<Boolean> {
        return progressLiveData
    }

    /**
     * Метод для получения инстанса LiveData
     *
     * @return LiveData с [Throwable]
     */
    fun getErrorLiveData(): LiveData<Throwable> {
        return errorLiveData
    }

    /**
     * Метод для получения инстанса LiveData
     *
     * @return LiveData со списком моделей [PhotoItem]
     */
    fun getPhotoItemsLiveData(): LiveData<List<PhotoItem>> {
        return photoItemsLiveData
    }

    fun getFavPhotoItemsLiveData(): LiveData<List<PhotoItem>> {
        return favPhotoItemsLiveData
    }

    /**
     * Метод для получения инстанса LiveData.
     *
     * @return LiveData с [Boolean]
     */
    fun getRefreshingProgressLiveData(): LiveData<Boolean> {
        return refreshLiveData
    }

    private fun updateContent() {
        when(photosInteractor.getGalleryType()) {
            GalleryType.RANDOM.ordinal -> null
            GalleryType.FAVOURITE.ordinal -> getFavouritePhotos()
        }
    }
}
