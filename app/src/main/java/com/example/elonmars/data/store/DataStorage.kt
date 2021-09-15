package com.example.elonmars.data.store

import android.content.SharedPreferences
import com.example.elonmars.data.model.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.model.WeatherItem

/**
 * Реализация хранилища данных.
 *
 * @param preferences преференсы для хранения данных
 */
class DataStorage(private val preferences: SharedPreferences) : DataPreferences(), IDataStorage {

    companion object {
        private const val PHOTOS_KEY = "PHOTOS_KEY"
        private const val FAVOURITE_PHOTOS_KEY = "FAVOURITE_PHOTOS_KEY"
        private const val WEATHER_KEY = "WEATHER_KEY"
        private const val WEATHER_ITEM_KEY = "WEATHER_ITEM_KEY"
        private const val LATEST_WEATHER_DAY_KEY = "LATEST_WEATHER_DAY_KEY"
        private const val CONVERSION_KEY = "CONVERSION_KEY"
        private const val END_MILLIS_KEY = "END_MILLIS_KEY"
        private const val TIMER_STATE_KEY = "TIMER_STATE_KEY"
    }

    /** Список с моделями [WeatherDataItem] */
    override var weatherDataItems: List<WeatherDataItem>?
        get() = preferences.getString(WEATHER_KEY, null)?.let { getDataFromJson(it) }
        set(value) = preferences.edit().putString(WEATHER_KEY, convertDataToJson(value)).apply()

    override var weatherItems: List<WeatherItem>?
        get() = preferences.getString(WEATHER_ITEM_KEY, null)?.let { getDataFromJson(it) }
        set(value) = preferences.edit().putString(WEATHER_ITEM_KEY, convertDataToJson(value))
            .apply()

    override var latestWeatherDay: WeatherItem?
        get() = preferences.getString(LATEST_WEATHER_DAY_KEY, null)?.let { getItemFromJson(it) }
        set(value) = preferences.edit().putString(LATEST_WEATHER_DAY_KEY, convertItemToJson(value))
            .apply()

    /** Список с моделями [PhotoItem] */
    override var photos: List<PhotoItem>?
        get() = preferences.getString(PHOTOS_KEY, null)?.let { getDataFromJson(it) }
        set(value) = preferences.edit().putString(PHOTOS_KEY, convertDataToJson(value)).apply()

    override var favouritePhotos: List<PhotoItem>?
        get() = preferences.getString(FAVOURITE_PHOTOS_KEY, null)?.let { getDataFromJson(it) }
        set(value) = preferences.edit().putString(FAVOURITE_PHOTOS_KEY, convertDataToJson(value))
            .apply()

    /** Флаг для обозначения выбранного режима конвертации температуры */
    override var farenheitEnabled: Boolean
        get() = preferences.getBoolean(CONVERSION_KEY, false)
        set(value) = preferences.edit().putBoolean(CONVERSION_KEY, value).apply()

    override var endMillis: Long
        get() = preferences.getLong(END_MILLIS_KEY, 0)
        set(value) = preferences.edit().putLong(END_MILLIS_KEY, value).apply()

    override var timerState: Int
        get() = preferences.getInt(TIMER_STATE_KEY, 0)
        set(value) = preferences.edit().putInt(TIMER_STATE_KEY, value).apply()
}
