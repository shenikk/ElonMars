package com.example.elonmars.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.WeatherItem
import com.example.elonmars.data.repository.ItemsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
//FIXME
/**
 * ViewModel экрана с информацией о погоде за последние 10 дней.
 */
class WeatherViewModel : ViewModel() {

    private val TAG = "WeatherViewModel"
    private var disposable: Disposable? = null
    private val itemsRepository = ItemsRepository()

    private val shimmerLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val weatherItemsLiveData = MutableLiveData<ArrayList<WeatherItem>>()


    /**
     * Метод для асинхронной загрузки списка фильмов.
     */
    fun loadDataAsync() {
        disposable = itemsRepository.loadDataAsync()
            .doOnSubscribe {
                shimmerLiveData.postValue(true)
            }
            .doAfterTerminate { shimmerLiveData.postValue(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(weatherItemsLiveData::setValue, errorLiveData::setValue)
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
     * @return LiveData со списком моделей [WeatherItem]
     */
    fun getWeatherItemsLiveData(): LiveData<ArrayList<WeatherItem>> {
        return weatherItemsLiveData
    }
}