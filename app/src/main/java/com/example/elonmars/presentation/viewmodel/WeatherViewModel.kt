package com.example.elonmars.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.data.model.WeatherDataItem
import com.example.elonmars.domain.provider.ISchedulersProvider
import com.example.elonmars.domain.interactors.IWeatherInteractor
import com.example.elonmars.presentation.extensions.logDebug
import com.example.elonmars.presentation.model.WeatherItem
import io.reactivex.disposables.Disposable

/**
 * ViewModel экрана с информацией о погоде за последние 10 дней.
 *
 * @param itemsRepository репозиторий с данными о погоде
 * @param schedulersProvider
 *
 * @testClass unit: WeatherViewModelTest
 */
class WeatherViewModel(
    private val weatherInteractor: IWeatherInteractor,
    private val schedulersProvider: ISchedulersProvider
) : ViewModel() {

    private var disposable: Disposable? = null

    private val shimmerLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val weatherItemsLiveData = MutableLiveData<List<WeatherItem>>()

    private var latestDayLiveData = MutableLiveData<WeatherItem>()
    private val refreshLiveData = MutableLiveData<Boolean>()

    /**
     * Метод для асинхронной загрузки списка фильмов.
     */
    fun loadDataAsync() {
        disposable = weatherInteractor.loadDataAsync()
            .doOnSubscribe {
                shimmerLiveData.postValue(true)
            }
            .doAfterTerminate { shimmerLiveData.postValue(false) }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe({ doOnSuccess() }, errorLiveData::setValue)
    }

    fun loadDataOnForceAsync() {
        disposable = weatherInteractor.loadDataAsyncOnCall()
            .doOnSubscribe {
                refreshLiveData.postValue(true)
            }
            .doAfterTerminate { refreshLiveData.postValue(false) }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe({ doOnSuccess() }, errorLiveData::setValue)
    }

    fun convertTemperature() {
        weatherInteractor.convertTemperature()

        weatherItemsLiveData.value = weatherInteractor.getWeatherItems()
        latestDayLiveData.value = weatherInteractor.getLatestWeatherDay()
    }

    private fun doOnSuccess() {
        weatherItemsLiveData.value = weatherInteractor.getWeatherItems()
        latestDayLiveData.value = weatherInteractor.getLatestWeatherDay()
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
    fun getShimmerLiveData(): LiveData<Boolean> {
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
     * @return LiveData со списком моделей [WeatherDataItem]
     */
    fun getWeatherItemsLiveData(): LiveData<List<WeatherItem>> {
        return weatherItemsLiveData
    }

    /**
     * Метод для получения инстанса LiveData
     *
     * @return LiveData с моделью [WeatherItem]
     */
    fun getLatestDayLiveData(): LiveData<WeatherItem> {
        return latestDayLiveData
    }

    fun getRefreshLiveData(): LiveData<Boolean> {
        return refreshLiveData
    }
}
