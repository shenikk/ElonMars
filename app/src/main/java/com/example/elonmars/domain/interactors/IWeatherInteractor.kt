package com.example.elonmars.domain.interactors

import com.example.elonmars.presentation.model.WeatherItem
import io.reactivex.Single

/** Интерфейс интерактора для экрана с погодой. */
interface IWeatherInteractor {

    /**
     * Асихронно загружает погодные данные.
     *
     * @return Single<Unit>
     */
    fun loadDataAsync(): Single<Unit>

    /**
     * Асихронно загружает погодные данные по действию пользователя pull to refresh.
     *
     * @return Single<Unit>
     */
    fun loadDataAsyncOnCall(): Single<Unit>

    /**
     * Конвертирует температуру в определенную единицу измерения.
     */
    fun convertTemperature()

    /**
     * Возвращает список моделей [WeatherItem].
     *
     * @return список моделей [WeatherItem].
     */
    fun getWeatherItems(): List<WeatherItem>?

    /**
     * Возвращает последний доступный [WeatherItem] из списка.
     *
     * @return модель [WeatherItem].
     */
    fun getLatestWeatherDay(): WeatherItem?
}
