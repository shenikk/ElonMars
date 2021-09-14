package com.example.elonmars.data.store

import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem

interface IDataStorage {
    var weatherDataItems: List<WeatherDataItem>?
    var photos: List<PhotoItem>?
    var favouritePhotos: List<PhotoItem>?
    var farenheitEnabled: Boolean
    var endMillis: Long
    var timerState: Int
}
