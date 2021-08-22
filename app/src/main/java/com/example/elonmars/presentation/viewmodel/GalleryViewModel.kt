package com.example.elonmars.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.data.provider.SchedulersProvider
import com.example.elonmars.data.repository.ItemsRepository
import com.example.elonmars.data.store.DataStorageImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * ViewModel экрана со списком фильмов.
 *
 * @param itemsRepository репозиторий с данными о фото
 * @param schedulersProvider
 */
class GalleryViewModel(
    private val itemsRepository: ItemsRepository,
    private val schedulersProvider: SchedulersProvider
) : ViewModel() {

    private val TAG = "GalleryViewModel"
    private var disposable: Disposable? = null

    private val shimmerLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val photoItemsLiveData = MutableLiveData<ArrayList<PhotoItem>>()
    private val refreshLiveData = MutableLiveData<Boolean>()

    /**
     * Метод для асинхронной загрузки списка фото.
     */
    fun loadDataAsync() {
        disposable = itemsRepository.loadPhotosAsync()
            .doOnSubscribe {
                shimmerLiveData.postValue(true)
            }
            .doAfterTerminate { shimmerLiveData.postValue(false) }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(photoItemsLiveData::setValue, errorLiveData::setValue)
    }

    /**
     * Метод для асинхронной загрузки списка фото после действия pull to refresh.
     */
    fun loadDataOnForceAsync() {
        disposable = itemsRepository.loadPhotosOnCall()
            .doOnSubscribe {
                refreshLiveData.postValue(true)
            }
            .doAfterTerminate { refreshLiveData.postValue(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(photoItemsLiveData::setValue, errorLiveData::setValue)
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        Log.d(TAG, "onCleared() called")
    }

    /**
     * Метод для получения инстанса LiveData
     *
     * @return LiveData с [Boolean]
     */
    fun getProgressLiveData(): LiveData<Boolean> {
        return shimmerLiveData
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
    fun getPhotoItemsLiveData(): LiveData<ArrayList<PhotoItem>> {
        return photoItemsLiveData
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