package com.example.elonmars.data.provider

import com.example.elonmars.WeatherDataItem

/** Адрес для загрузки картинок */
const val PHOTOS_BASE_URL = "https://api.nasa.gov/"

/** Адрес для информации о погоде на Марсе */
const val WEATHER_BASE_URL = "https://mars.nasa.gov/"

/** Индивидуальный ключ для доступа к api nasa */
const val API_KEY = "S0pO0YrtiG1iqfuIokCyaD1xM0D4xob8ywxlM6uf"

/**
 * Интерфейс провайдера данных об информации о погоде.
 */
interface IWeatherItemProvider {

    /**
     * Метод для загрузки информации о погоде.
     *
     * @return список моделей [WeatherDataItem] описывающих информацию о погоде за конкретный день.
     */
    fun loadWeatherItemsList(): ArrayList<WeatherDataItem>
}