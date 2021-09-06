package com.example.elonmars.data.provider

import com.example.elonmars.WeatherDataItem

/**
 * Интерфейс провайдера данных об информации о погоде.
 */
interface IWeatherItemsProvider {

    /**
     * Метод для загрузки информации о погоде.
     *
     * @return список моделей [WeatherDataItem] описывающих информацию о погоде за конкретный день.
     */
    fun loadWeatherItemsList(): ArrayList<WeatherDataItem>
}
