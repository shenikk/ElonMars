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
 * @param weatherInteractor интерактор обрабатывающий данные о погоде.
 * @param schedulersProvider провайдер с Scheduler для работы на разных потоках.
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

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        logDebug("onCleared() called")
    }

    /**
     * Метод для асинхронной загрузки погодных данных.
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

    /**
     * Метод для асинхронной загрузки погодных данных по действию пользователя pull to refresh.
     */
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

    /**
     * Метод конвертируют температуру в определенную единицу измерения.
     */
    fun convertTemperature() {
        weatherInteractor.convertTemperature()

        weatherItemsLiveData.value = weatherInteractor.getWeatherItems()
        latestDayLiveData.value = weatherInteractor.getLatestWeatherDay()
    }

    /**
     * Метод для получения инстанса LiveData.
     *
     * @return LiveData с [Boolean].
     */
    fun getShimmerLiveData(): LiveData<Boolean> {
        return shimmerLiveData
    }

    /**
     * Метод для получения инстанса LiveData.
     *
     * @return LiveData с [Throwable].
     */
    fun getErrorLiveData(): LiveData<Throwable> {
        return errorLiveData
    }

    /**
     * Метод для получения инстанса LiveData.
     *
     * @return LiveData со списком моделей [WeatherDataItem].
     */
    fun getWeatherItemsLiveData(): LiveData<List<WeatherItem>> {
        return weatherItemsLiveData
    }

    /**
     * Метод для получения инстанса LiveData.
     *
     * @return LiveData с моделью [WeatherItem].
     */
    fun getLatestDayLiveData(): LiveData<WeatherItem> {
        return latestDayLiveData
    }

    /**
     * Метод для получения инстанса LiveData.
     *
     * @return LiveData с [Boolean].
     */
    fun getRefreshLiveData(): LiveData<Boolean> {
        return refreshLiveData
    }

    private fun doOnSuccess() {
        weatherItemsLiveData.value = weatherInteractor.getWeatherItems()
        latestDayLiveData.value = weatherInteractor.getLatestWeatherDay()
    }
}
