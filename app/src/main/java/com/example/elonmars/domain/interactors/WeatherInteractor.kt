package com.example.elonmars.domain.interactors

import com.example.elonmars.data.model.WeatherDataItem
import com.example.elonmars.data.store.IDataStorage
import com.example.elonmars.domain.repositories.IItemsRepository
import com.example.elonmars.presentation.extensions.getFirstItem
import com.example.elonmars.presentation.extensions.logError
import com.example.elonmars.presentation.weather.model.WeatherItem
import io.reactivex.Single

/**
 * Интерактор для экрана с погодой.
 *
 * @param itemsRepository репозиторий с погодными данными
 * @param dataStorage хранилище данных
 *
 * @testClass unit: TaskInteractorTest
 */
class WeatherInteractor(
    private val itemsRepository: IItemsRepository,
    private val dataStorage: IDataStorage
) : IWeatherInteractor {

    private val weatherItemsData = mutableListOf<WeatherDataItem>()

    override fun loadDataAsync(): Single<Unit> {
        return itemsRepository.loadDataAsync()
            .map {
                doOnSuccess(it)
            }
    }

    override fun loadDataAsyncOnCall(): Single<Unit> {
        return itemsRepository.loadDataAsyncOnCall()
            .map {
                doOnSuccess(it)
            }
    }

    override fun convertTemperature() {
        weatherItemsData.getFirstItem()?.let { item ->
            if (dataStorage.fahrenheitEnabled) {

                dataStorage.weatherItems = weatherItemsData.map { convertFarenheit(it) }
                dataStorage.latestWeatherDay = convertFarenheit(item)

            } else {
                dataStorage.weatherItems = weatherItemsData.map { convertCelsius(it) }
                dataStorage.latestWeatherDay = convertCelsius(item)
            }
        }
    }

    override fun getLatestWeatherDay() = dataStorage.latestWeatherDay

    override fun getFahrenheitEnabled() = dataStorage.fahrenheitEnabled

    override fun setFahrenheitEnabled(enabled: Boolean) {
        dataStorage.fahrenheitEnabled = enabled
    }

    override fun getWeatherItems() = dataStorage.weatherItems

    private fun doOnSuccess(weatherDataItemList: List<WeatherDataItem>) {
        weatherItemsData.addAll(weatherDataItemList)
        val convertedValues = weatherDataItemList.map { convertAfterServerDownload(it) }

        dataStorage.weatherItems = convertedValues
        dataStorage.latestWeatherDay =
            convertedValues.getFirstItem() ?: WeatherItem("NA", "NA", "NA", "NA")
    }

    private fun convertAfterServerDownload(weatherDataItem: WeatherDataItem): WeatherItem {
        return if (dataStorage.fahrenheitEnabled) {
            convertFarenheit(weatherDataItem)
        } else {
            convertCelsius(weatherDataItem)
        }
    }

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
            logError("$temperature is not a number")
            null
        }
    }
}
