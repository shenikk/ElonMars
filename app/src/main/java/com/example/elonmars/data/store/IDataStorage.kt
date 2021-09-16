package com.example.elonmars.data.store

import com.example.elonmars.data.model.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.model.WeatherItem

interface IDataStorage {
    var weatherDataItems: List<WeatherDataItem>?
    var weatherItems: List<WeatherItem>?
    var latestWeatherDay: WeatherItem?
    var photos: List<PhotoItem>?
    var favouritePhotos: List<PhotoItem>?
    var farenheitEnabled: Boolean
    var endMillis: Long
    var timerState: Int
    var contentType: Int
}
