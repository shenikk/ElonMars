package com.example.elonmars.data.store

import com.example.elonmars.WeatherDataItem
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.view.HomeFragment

interface IDataStorage {
    var weatherDataItems: ArrayList<WeatherDataItem>?
    var photos: ArrayList<PhotoItem>?
    var favouritePhotos: ArrayList<PhotoItem>?
    var farenheitEnabled: Boolean
    var endMillis: Long
    var timerState: Int
}
