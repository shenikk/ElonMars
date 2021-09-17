package com.example.elonmars.data.store

import com.example.elonmars.data.model.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.home.enums.TimerState
import com.example.elonmars.presentation.weather.model.WeatherItem

/** Интерфейс хранилища данных */
interface IDataStorage {

    /** Список с моделями [WeatherDataItem] */
    var weatherDataItems: List<WeatherDataItem>?

    /** Список с моделями [WeatherItem] */
    var weatherItems: List<WeatherItem>?

    /** Последний доступный [WeatherItem] */
    var latestWeatherDay: WeatherItem?

    /** Список с моделями [PhotoItem] */
    var photos: List<PhotoItem>?

    /** Список с моделями [PhotoItem], которые помечены пользователем как 'любимые' */
    var favouritePhotos: List<PhotoItem>?

    /** Флаг для обозначения выбранного режима конвертации температуры */
    var fahrenheitEnabled: Boolean

    /** Время до события в миллисекундах */
    var endMillis: Long

    /**
     * Состояние таймера,
     * представленное порядковым номером константы перечисления [TimerState].
     */
    var timerState: Int

    /**
     * Состояние экрана галереи,
     * представленное порядковым номером константы перечисления [TimerState].
     */
    var contentType: Int
}
