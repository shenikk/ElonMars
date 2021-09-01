package com.example.elonmars.data.store

import android.content.SharedPreferences
import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem

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
        private const val CONVERSION_KEY = "CONVERSION_KEY"
    }

    /** Список с моделями [WeatherDataItem] */
    override var weatherDataItems: ArrayList<WeatherDataItem>?
        get() = preferences.getString(WEATHER_KEY, null)?.let { getDataFromJson(it) }
        set(value) = preferences.edit().putString(WEATHER_KEY, convertDataToJson(value)).apply()

    /** Список с моделями [PhotoItem] */
    override var photos: ArrayList<PhotoItem>?
        get() = preferences.getString(PHOTOS_KEY, null)?.let { getDataFromJson(it) }
        set(value) = preferences.edit().putString(PHOTOS_KEY, convertDataToJson(value)).apply()

    override var favouritePhotos: ArrayList<PhotoItem>?
        get() = preferences.getString(FAVOURITE_PHOTOS_KEY, null)?.let { getDataFromJson(it) }
        set(value) = preferences.edit().putString(FAVOURITE_PHOTOS_KEY, convertDataToJson(value)).apply()

    /** Флаг для обозначения выбранного режима конвертации температуры */
    override var farenheitEnabled: Boolean
        get() = preferences.getBoolean(CONVERSION_KEY, false)
        set(value) = preferences.edit().putBoolean(CONVERSION_KEY, value).apply()
}
