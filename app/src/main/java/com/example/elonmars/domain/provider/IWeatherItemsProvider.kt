package com.example.elonmars.domain.provider

import com.example.elonmars.data.model.WeatherDataItem

/**
 * Интерфейс провайдера данных об информации о погоде.
 */
interface IWeatherItemsProvider {

    /**
     * Метод для загрузки информации о погоде.
     *
     * @return список моделей [WeatherDataItem] описывающих информацию о погоде за конкретный день.
     */
    fun loadWeatherItemsList(): List<WeatherDataItem>
}
