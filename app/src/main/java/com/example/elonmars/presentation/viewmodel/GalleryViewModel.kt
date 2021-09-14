package com.example.elonmars.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.data.provider.ISchedulersProvider
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

    private var galleryType: GalleryType = GalleryType.RANDOM

    /**
     * Метод для асинхронной загрузки списка фото.
     */
    fun loadDataAsync() {
        disposable = photosInteractor.loadPhotosAsync()
            .doOnSubscribe {
                progressLiveData.postValue(true)
            }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .map {
                return@map it.filter { photo ->
                    photo.media_type == "image"
                }
            }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(photoItemsLiveData::setValue, errorLiveData::setValue)
    }

    /**
     * Метод для асинхронной загрузки списка фото после действия pull to refresh.
     */
    fun loadDataOnForceAsync() {
        disposable = photosInteractor.loadPhotosOnCall()
            .doOnSubscribe {
                refreshLiveData.postValue(true)
            }
            .doAfterTerminate { refreshLiveData.postValue(false) }
            .map {
                return@map it.filter { photo ->
                    photo.media_type == "image"
                }
            }
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

    private fun updateContent() {
        when(galleryType) {
            GalleryType.RANDOM -> null
            GalleryType.FAVOURITE -> getFavouritePhotos()
        }
    }

    fun setContentType(galleryType: GalleryType) {
        this.galleryType = galleryType
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        logDebug("onCleared() called")
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
}
