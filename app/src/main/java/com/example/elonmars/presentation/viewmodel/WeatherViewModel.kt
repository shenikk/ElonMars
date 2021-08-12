package com.example.elonmars.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.repository.ItemsRepository
import com.example.elonmars.presentation.model.WeatherItem
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
    private val weatherItemsLiveData = MutableLiveData<List<WeatherItem>>()

    private val weatherItemsData = mutableListOf<WeatherDataItem>()
    private var isFarenheitShown = false
    private var latestDayLiveData = MutableLiveData<WeatherItem>()

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
            .subscribe({ doOnSuccess(it) }, errorLiveData::setValue)
    }

    fun convertTemperature() {
        if (isFarenheitShown) {
            weatherItemsLiveData.value = weatherItemsData.map { convertCelsius(it) }
            latestDayLiveData.value = convertCelsius(weatherItemsData[0])
        } else {
            weatherItemsLiveData.value = weatherItemsData.map { convertFarenheit(it) }
            latestDayLiveData.value = convertFarenheit(weatherItemsData[0])
        }
        isFarenheitShown = !isFarenheitShown
    }

    private fun doOnSuccess(weatherDataItemList: ArrayList<WeatherDataItem>) {
        weatherItemsData.addAll(weatherDataItemList)
        val convertedValues = weatherDataItemList.map { convertCelsius(it) }
        weatherItemsLiveData.value = convertedValues
        latestDayLiveData.value = convertedValues[0]
    }

    // TODO избавиться от null в WeatherData
    private fun convertFarenheit(weatherDataItem: WeatherDataItem): WeatherItem {

        val highTemp = convertToFarenheit(weatherDataItem.highTemp) ?: "NA"
        val lowTemp = convertToFarenheit(weatherDataItem.lowTemp) ?: "NA"

        return WeatherItem(
            "Sol ${weatherDataItem.weatherDay}",
            weatherDataItem.earthDate,
            "High: $highTemp °F",
            "Low: $lowTemp °F"
        )
    }

    private fun convertCelsius(weatherDataItem: WeatherDataItem): WeatherItem {

        return WeatherItem(
            "Sol ${weatherDataItem.weatherDay}",
            weatherDataItem.earthDate,
            "High: ${weatherDataItem.highTemp} °C",
            "Low: ${weatherDataItem.lowTemp} °C"
        )
    }

    private fun convertToFarenheit(temperature: String?): String? {
        return try {
            (temperature?.toFloat()?.let { (it * 9f / 5f) + 32f })?.toInt().toString()
        } catch (e: Exception) {
            null
        }
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
}