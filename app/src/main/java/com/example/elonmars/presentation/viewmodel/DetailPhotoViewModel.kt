package com.example.elonmars.presentation.viewmodel

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.data.provider.ISchedulersProvider
import com.example.elonmars.domain.interactors.IPhotosInteractor
import com.example.elonmars.presentation.extensions.LogDebug
import io.reactivex.disposables.Disposable

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

    private var disposable: Disposable? = null

    private val progressLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val photoLiveData = MutableLiveData<PhotoItem>()
    private val favPhotoItemsLiveData = MutableLiveData<ArrayList<PhotoItem>>()
    private val refreshLiveData = MutableLiveData<Boolean>()

    /**
     * Метод для асинхронной загрузки фото.
     */
    fun loadPhotoAsync(view: ImageView, image: String) {
        photosInteractor.loadPhoto(view, image)
    }

    fun setFavourite(photoItem: PhotoItem) {
        photosInteractor.setFavourite(photoItem)
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        LogDebug("onCleared() called")
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
    fun getPhotoItemsLiveData(): LiveData<PhotoItem> {
        return photoLiveData
    }

    fun getFavPhotoItemsLiveData(): LiveData<ArrayList<PhotoItem>> {
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
